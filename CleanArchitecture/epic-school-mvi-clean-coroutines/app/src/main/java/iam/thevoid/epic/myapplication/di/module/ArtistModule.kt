package iam.thevoid.epic.myapplication.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import iam.thevoid.epic.myapplication.data.repositories.artist.ArtistRepositoryImpl
import iam.thevoid.epic.myapplication.domain.interactor.artist.ArtistInteractor
import iam.thevoid.epic.myapplication.domain.interactor.artist.ArtistInteractorImpl
import iam.thevoid.epic.myapplication.domain.repositories.artist.ArtistRepository

@InstallIn(SingletonComponent::class)
@Module
abstract class ArtistModule {
    @Binds
    abstract fun getArtistInteractor(impl: ArtistInteractorImpl): ArtistInteractor

    @Binds
    abstract fun getArtistRepository(impl: ArtistRepositoryImpl): ArtistRepository
}