package com.sedsoftware.yaptalker.presentation.feature.news.di

import com.sedsoftware.yaptalker.data.repository.YapNewsRepository
import com.sedsoftware.yaptalker.data.repository.YapThumbnailRepository
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.NewsInteractor
import com.sedsoftware.yaptalker.domain.interactor.VideoThumbnailsInteractor
import com.sedsoftware.yaptalker.domain.repository.NewsRepository
import com.sedsoftware.yaptalker.domain.repository.ThumbnailRepository
import com.sedsoftware.yaptalker.presentation.base.thumbnail.ThumbnailsLoader
import com.sedsoftware.yaptalker.presentation.feature.news.NewsFragment
import com.sedsoftware.yaptalker.presentation.feature.news.NewsPresenter
import com.sedsoftware.yaptalker.presentation.feature.news.adapter.NewsItemElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.NewsModelMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Module
abstract class NewsFragmentModule {

  @Module
  companion object {

    @FragmentScope
    @Provides
    @JvmStatic
    fun providePresenter(router: Router,
                         settings: Settings,
                         newsInteractor: NewsInteractor,
                         thumbnailsInteractor: VideoThumbnailsInteractor,
                         mapper: NewsModelMapper): NewsPresenter =
      NewsPresenter(router, settings, newsInteractor, thumbnailsInteractor, mapper)
  }

  @FragmentScope
  @Binds
  abstract fun newsRepository(repo: YapNewsRepository): NewsRepository

  @FragmentScope
  @Binds
  abstract fun newsThumbnailsRepository(repo: YapThumbnailRepository): ThumbnailRepository

  @FragmentScope
  @Binds
  abstract fun newsThumbnailsLoader(fragment: NewsFragment): ThumbnailsLoader

  @FragmentScope
  @Binds
  abstract fun newsElementsClickListener(presenter: NewsPresenter): NewsItemElementsClickListener
}
