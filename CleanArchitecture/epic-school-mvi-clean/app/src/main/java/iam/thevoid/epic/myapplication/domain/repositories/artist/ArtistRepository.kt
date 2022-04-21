package iam.thevoid.epic.myapplication.domain.repositories.artist

import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import io.reactivex.rxjava3.core.Single

interface ArtistRepository {
    fun requestPage(query: String, page: Int = 1): Single<ArtistsPage>
}