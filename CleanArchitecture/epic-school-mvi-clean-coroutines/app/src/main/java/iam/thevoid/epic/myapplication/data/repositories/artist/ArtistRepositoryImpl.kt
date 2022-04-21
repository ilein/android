package iam.thevoid.epic.myapplication.data.repositories.artist

import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import iam.thevoid.epic.myapplication.data.network.music.MusicbrainzClient
import iam.thevoid.epic.myapplication.domain.repositories.artist.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor() : ArtistRepository {
    override fun requestPage(query: String, page: Int): Flow<ArtistsPage> {
        return flow {
            emit(
                MusicbrainzClient.getPageSuspend(
                    query,
                    offset = (page - 1) * MusicbrainzClient.PAGE_SIZE
                )
            )
        }
    }
}