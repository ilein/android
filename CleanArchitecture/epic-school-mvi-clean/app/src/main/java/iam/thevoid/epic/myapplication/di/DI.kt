package iam.thevoid.epic.myapplication.di

import iam.thevoid.epic.myapplication.data.repositories.artist.ArtistRepositoryImpl
import iam.thevoid.epic.myapplication.domain.interactor.artist.ArtistInteractor
import iam.thevoid.epic.myapplication.domain.interactor.artist.ArtistInteractorImpl
import iam.thevoid.epic.myapplication.domain.repositories.artist.ArtistRepository

object DI {
    private val repository : ArtistRepository = ArtistRepositoryImpl()
    val artistInteractor: ArtistInteractor = ArtistInteractorImpl(repository)
}