package com.example.epicdatabaseexample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.epicdatabaseexample.db.PersonListStore
import com.example.epicdatabaseexample.db.person.PersonDao
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(
    private val personDao: PersonDao
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val personDao: PersonDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainActivityViewModel(personDao) as T
        }
    }

    private val compositeDisposable = CompositeDisposable()

    fun init() {
        fillPersonTableIfNeed()
    }

    // Заполняем локальную базу данных информацией о доступных сотрудниках.
    // В реальном проекте можно представить, что здесь выполняется сетевой запрос,
    // чтобы обновить список для работы с базой.
    private fun fillPersonTableIfNeed() {
        compositeDisposable.add(
            personDao.requirePersonList()
                .subscribeOn(Schedulers.io())
                .flatMapCompletable { personList ->
                    if (personList.isEmpty()) {
                        personDao.insertPersonList(
                            PersonListStore.personList
                        )
                    } else {
                        Completable.complete()
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}