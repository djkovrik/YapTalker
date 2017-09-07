package com.sedsoftware.yaptalker.di

import com.sedsoftware.yaptalker.di.modules.ApplicationModule
import com.sedsoftware.yaptalker.di.modules.DataManagerModule
import com.sedsoftware.yaptalker.di.modules.NetworkingModule
import com.sedsoftware.yaptalker.di.modules.ThumbnailsLoaderModule
import com.sedsoftware.yaptalker.features.forum.ChosenForumPresenter
import com.sedsoftware.yaptalker.features.forumslist.ForumsPresenter
import com.sedsoftware.yaptalker.features.imagedisplay.ImageDisplayPresenter
import com.sedsoftware.yaptalker.features.navigation.NavigationViewPresenter
import com.sedsoftware.yaptalker.features.news.NewsAdapter
import com.sedsoftware.yaptalker.features.news.NewsPresenter
import com.sedsoftware.yaptalker.features.topic.ChosenTopicAdapter
import com.sedsoftware.yaptalker.features.topic.ChosenTopicPresenter
import dagger.Component
import javax.inject.Singleton

// TODO() Rearrange modules to different Subcomponents
// TODO() Migrate from Dagger to kodeIn maybe?

@Singleton
@Component(modules = arrayOf(
    ApplicationModule::class,
    DataManagerModule::class,
    NetworkingModule::class,
    ThumbnailsLoaderModule::class))

interface ApplicationComponent {
  // Injections here
  fun inject(adapter: NewsAdapter)
  fun inject(adapter: ChosenTopicAdapter)
  fun inject(presenter: NavigationViewPresenter)
  fun inject(presenter: NewsPresenter)
  fun inject(presenter: ForumsPresenter)
  fun inject(presenter: ChosenForumPresenter)
  fun inject(presenter: ChosenTopicPresenter)
  fun inject(presenter: ImageDisplayPresenter)
}
