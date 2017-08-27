package com.sedsoftware.yaptalker

import com.sedsoftware.yaptalker.di.ApplicationComponent
import com.sedsoftware.yaptalker.features.forum.ChosenForumPresenter
import com.sedsoftware.yaptalker.features.forumslist.ForumsPresenter
import com.sedsoftware.yaptalker.features.news.NewsAdapter
import com.sedsoftware.yaptalker.features.news.NewsPresenter
import com.sedsoftware.yaptalker.features.topic.ChosenTopicPresenter

open class TestComponent: ApplicationComponent {

  override fun inject(presenter: NewsPresenter) {
  }

  override fun inject(presenter: ForumsPresenter) {
  }

  override fun inject(presenter: ChosenForumPresenter) {
  }

  override fun inject(presenter: ChosenTopicPresenter) {
  }

  override fun inject(adapter: NewsAdapter) {
  }
}