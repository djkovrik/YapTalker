package com.sedsoftware.yaptalker.features.news

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.remote.YapDataManager
import com.sedsoftware.yaptalker.features.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class NewsPresenter: BasePresenter<NewsView>() {

  @Inject
  lateinit var yapDataManager: YapDataManager

  init {
    YapTalkerApp.appComponent.inject(this)
  }

}