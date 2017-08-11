package com.sedsoftware.yaptalker

import com.sedsoftware.yaptalker.di.ApplicationComponent
import com.sedsoftware.yaptalker.features.news.NewsAdapter
import com.sedsoftware.yaptalker.features.news.NewsPresenter

open class TestComponent: ApplicationComponent {

  override fun inject(presenter: NewsPresenter) {
  }

  override fun inject(adapter: NewsAdapter) {
  }
}