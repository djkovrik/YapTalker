package com.sedsoftware.yaptalker.presentation.feature.gallery.di

import com.sedsoftware.yaptalker.data.repository.YapChosenTopicRepository
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.device.sharing.YapSharingHelper
import com.sedsoftware.yaptalker.device.storage.YapImageStorage
import com.sedsoftware.yaptalker.di.scope.ActivityScope
import com.sedsoftware.yaptalker.domain.device.ImageStorage
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.device.SharingHelper
import com.sedsoftware.yaptalker.domain.interactor.ImageHelperInteractor
import com.sedsoftware.yaptalker.domain.interactor.TopicGalleryInteractor
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import com.sedsoftware.yaptalker.presentation.feature.gallery.TopicGalleryActivity
import com.sedsoftware.yaptalker.presentation.feature.gallery.TopicGalleryPresenter
import com.sedsoftware.yaptalker.presentation.feature.gallery.adapter.TopicGalleryLoadMoreClickListener
import com.sedsoftware.yaptalker.presentation.feature.topic.GalleryInitialState
import com.sedsoftware.yaptalker.presentation.mapper.TopicGalleryModelMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

@Module
abstract class TopicGalleryActivityModule {

    @Module
    companion object {

        @ActivityScope
        @Provides
        @JvmStatic
        fun provideInitialGalleryState(activity: TopicGalleryActivity): GalleryInitialState =
            activity.galleryInitialState

        @ActivityScope
        @Provides
        @JvmStatic
        fun provideMessagesDelegate(activity: TopicGalleryActivity): MessagesDelegate =
            MessagesDelegate(WeakReference(activity))

        @ActivityScope
        @Provides
        @JvmStatic
        fun providePresenter(
            settings: Settings,
            galleryInteractor: TopicGalleryInteractor,
            imageHelper: ImageHelperInteractor,
            mapper: TopicGalleryModelMapper,
            initialState: GalleryInitialState,
            schedulers: SchedulersProvider
        ): TopicGalleryPresenter =
            TopicGalleryPresenter(settings, galleryInteractor, imageHelper, mapper, initialState, schedulers)
    }

    @ActivityScope
    @Binds
    abstract fun chosenTopicGalleryRepository(repo: YapChosenTopicRepository): ChosenTopicRepository

    @ActivityScope
    @Binds
    abstract fun galleryClickListener(presenter: TopicGalleryPresenter): TopicGalleryLoadMoreClickListener

    @ActivityScope
    @Binds
    abstract fun imageSharingHelper(helper: YapSharingHelper): SharingHelper

    @ActivityScope
    @Binds
    abstract fun imageStorage(storage: YapImageStorage): ImageStorage
}


