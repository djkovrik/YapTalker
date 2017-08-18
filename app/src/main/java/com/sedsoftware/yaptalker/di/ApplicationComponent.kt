package com.sedsoftware.yaptalker.di

import com.sedsoftware.yaptalker.di.modules.ApplicationModule
import com.sedsoftware.yaptalker.di.modules.DataManagerModule
import com.sedsoftware.yaptalker.di.modules.ThumbnailsLoaderModule
import com.sedsoftware.yaptalker.features.forum.ChosenForumPresenter
import com.sedsoftware.yaptalker.features.forumslist.ForumsPresenter
import com.sedsoftware.yaptalker.features.news.NewsAdapter
import com.sedsoftware.yaptalker.features.news.NewsPresenter
import dagger.Component
import javax.inject.Singleton

// TODO() Rearrange modules to different Subcomponents

@Singleton
@Component(modules = arrayOf(
    ApplicationModule::class,
    DataManagerModule::class,
    ThumbnailsLoaderModule::class))

interface ApplicationComponent {
  // Injections here
  fun inject(presenter: NewsPresenter)
  fun inject(adapter: NewsAdapter)
  fun inject(presenter: ForumsPresenter)
  fun inject(presenter: ChosenForumPresenter)
}
