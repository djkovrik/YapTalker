package com.sedsoftware.yaptalker.presentation.feature.incubator.di

import com.sedsoftware.yaptalker.data.repository.YapIncubatorRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.IncubatorInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoThumbnailsInteractor
import com.sedsoftware.yaptalker.domain.repository.IncubatorRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import com.sedsoftware.yaptalker.presentation.thumbnail.ThumbnailsLoader
import com.sedsoftware.yaptalker.presentation.feature.incubator.IncubatorFragment
import com.sedsoftware.yaptalker.presentation.feature.incubator.IncubatorPresenter
import com.sedsoftware.yaptalker.presentation.feature.incubator.adapter.IncubatorElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.IncubatorModelMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Module
abstract class IncubatorFragmentModule {

  @Module
  companion object {

    @FragmentScope
    @Provides
    @JvmStatic
    fun providePresenter(router: Router,
                         settings: Settings,
                         incubatorInteractor: IncubatorInteractor,
                         videoThumbnailsInteractor: VideoThumbnailsInteractor,
                         mapper: IncubatorModelMapper): IncubatorPresenter =
      IncubatorPresenter(router, settings, incubatorInteractor, videoThumbnailsInteractor, mapper)
  }

  @FragmentScope
  @Binds
  abstract fun incubatorRepository(repo: YapIncubatorRepository): IncubatorRepository

  @FragmentScope
  @Binds
  abstract fun incubatorThumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository

  @FragmentScope
  @Binds
  abstract fun incubatorThumbnailsLoader(fragment: IncubatorFragment): ThumbnailsLoader

  @FragmentScope
  @Binds
  abstract fun incubatorElementsClickListener(presenter: IncubatorPresenter): IncubatorElementsClickListener
}
