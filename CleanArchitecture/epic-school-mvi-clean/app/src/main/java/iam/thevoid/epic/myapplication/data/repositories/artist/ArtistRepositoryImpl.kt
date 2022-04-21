package iam.thevoid.epic.myapplication.data.repositories.artist

import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import iam.thevoid.epic.myapplication.data.network.music.MusicbrainzClient
import iam.thevoid.epic.myapplication.domain.repositories.artist.ArtistRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor() : ArtistRepository {
    override fun requestPage(query: String, page: Int): Single<ArtistsPage> {
        return MusicbrainzClient.getPage(query, offset = (page - 1) * MusicbrainzClient.PAGE_SIZE)
    }
}