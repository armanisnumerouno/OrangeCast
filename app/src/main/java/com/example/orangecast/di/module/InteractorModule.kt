package com.example.orangecast.di.module

import com.example.data.repository.FeedRepository
import com.example.data.repository.SearchRepository
import com.example.data.repository.SubscriptionsRepository
import com.example.orangecast.interactor.ArtistInteractor
import com.example.orangecast.interactor.GenresInteractor
import com.example.orangecast.interactor.SubscriptionInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModule {

    @Singleton
    @Provides
    fun provideDiscoverInteractor(repository: SearchRepository): GenresInteractor {
        return GenresInteractor(repository)
    }

    @Singleton
    @Provides
    fun provideChannelInteractor(searchRepository: SearchRepository, feedRepository: FeedRepository): ArtistInteractor {
        return ArtistInteractor(searchRepository, feedRepository)
    }

    @Singleton
    @Provides
    fun provideSubscriptionInteractor(subscriptionsRepository: SubscriptionsRepository): SubscriptionInteractor {
        return SubscriptionInteractor(subscriptionsRepository)
    }
}