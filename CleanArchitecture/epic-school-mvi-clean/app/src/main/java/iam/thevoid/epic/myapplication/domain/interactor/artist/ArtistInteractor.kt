package iam.thevoid.epic.myapplication.domain.interactor.artist

import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import iam.thevoid.epic.myapplication.domain.repositories.artist.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ArtistInteractor {
    fun requestPage(query: String, page: Int): Flow<ArtistsPage>
}


class ArtistInteractorImpl @Inject constructor(
    private val repository: ArtistRepository
) : ArtistInteractor {
    override fun requestPage(query: String, page: Int): Flow<ArtistsPage> {
        return flow {
            emit(
                repository.requestPage(query, page)
                    .map { artistPage ->
                        val modified =
                            artistPage.artists.map { a -> a.copy(name = "${a.name} Sample") }
                        artistPage.copy(artists = modified)
                    }.blockingGet()
            )
        }
    }
}