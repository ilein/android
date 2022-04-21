package iam.thevoid.epic.myapplication.domain.repositories.artist

import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    fun requestPage(query: String, page: Int = 1): Flow<ArtistsPage>
}