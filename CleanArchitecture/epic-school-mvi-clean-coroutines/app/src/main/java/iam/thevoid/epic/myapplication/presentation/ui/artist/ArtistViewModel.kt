package iam.thevoid.epic.myapplication.presentation.ui.artist

import android.os.Handler
import android.os.Looper
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iam.thevoid.epic.myapplication.data.model.musicbrnz.Artist
import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import iam.thevoid.epic.myapplication.data.network.country.CountriesClient
import iam.thevoid.epic.myapplication.domain.interactor.artist.ArtistInteractor
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val artistInteractor: ArtistInteractor
) : ViewModel() {

    val loading: ObservableBoolean = ObservableBoolean(false)

    val pagesCount: ObservableInt = ObservableInt(0)

    private val _liveData: MutableLiveData<ArtistViewState> = MutableLiveData(ArtistViewState())

    val state: LiveData<ArtistViewState> = _liveData

    private val sharedFlow = MutableSharedFlow<String>(1, 1)

    private val disposable = CompositeDisposable()

    private val flags = mutableMapOf<String, String>()

    init {
        sharedFlow
            .filter { it.isNotBlank() }
            .debounce(500)
            .flatMapLatest { query ->
                if (query.isBlank()) flowOf(ArtistsPage()) else requestPage(query)
            }
            .flowOn(Dispatchers.IO)
            .onEach(::onResponse)
            .launchIn(viewModelScope)
    }

    /**
     * _______________
     */

    /**
     * ACTIONS
     */

    fun onTextInput(text: String) {
        viewModelScope.launch {
            sharedFlow.emit(text)
        }
    }

    fun onSelectPage(page: Int) {
        updateState { it.copy(isFirstPage = page == 0) }
        viewModelScope.launch {
            sharedFlow.lastOrNull()?.let { query ->
                requestPage(query, page)
                    .onEach(::onResponse)
                    .collect()
            }
        }
    }

    /**
     * _______________
     */

    private fun onResponse(page: ArtistsPage) {
        pagesCount.set(page.count)
        updateState {
            it.copy(
                items = page.artists.map { ArtistData(it, flags[it.country]) },
                pagesCount = page.pagesCount,
                isFirstPage = page.isFirstPage
            )
        }

        val countries = page.artists.mapNotNull(Artist::country).toSet().toList()
        if (!countries.all(flags.keys::contains)) {
            requestFlags(countries - flags.keys)
        }
    }

    private fun requestFlags(countries: List<String>) {
        Observable.fromIterable(countries)
            .flatMapSingle { country ->
                CountriesClient.api.byCode(country)
                    .subscribeOn(Schedulers.io())
                    .map { Pair(country, it.flag) }
            }
            .toList()
            .subscribe({
                it.forEach { (country, flag) ->
                    flags[country] = flag
                }
                updateState { viewState ->
                    viewState.copy(
                        items = viewState.items.map { data ->
                            data.copy(flag = flags[data.artist.country])
                        }
                    )
                }
            }, Throwable::printStackTrace)
    }

    private fun requestPage(query: String, page: Int = 1): Flow<ArtistsPage> {
        return artistInteractor
            .requestPage(query, page)
            .flowOn(Dispatchers.IO)
            .onStart {
                loading.set(true)
                updateState { it.copy(loading = true) }
            }
            .onEach {
                loading.set(false)
                updateState { it.copy(loading = true) }
            }
            .retryWithDelay()
    }

    private fun <T> Flow<T>.retryWithDelay(
        delay: Long = 2000L,
        filter: (Throwable) -> Boolean = { true }
    ): Flow<T> {
        return retryWhen { cause, _ ->
            if (filter(cause)) {
                delay(delay)
                true
            } else {
                throw cause
            }
        }
    }

    private fun <T : Any> Single<T>.retryWithDelay(
        delay: Long = 2000L,
        filter: (Throwable) -> Boolean = { true }
    ): Single<T> {
        return retryWhen { errorThrowable ->
            errorThrowable.flatMap {
                if (filter(it)) {
                    Flowable.timer(delay, TimeUnit.MILLISECONDS)
                } else {
                    Flowable.error(it)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun updateState(update: (ArtistViewState) -> ArtistViewState) {
        Handler(Looper.getMainLooper()).post {
            _liveData.value?.also {
                _liveData.value = update(it)
            }
        }
    }
}