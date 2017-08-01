package com.sedsoftware.yaptalker.features.homeview

import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.remote.YapNewsLoader
import com.sedsoftware.yaptalker.features.base.BasePresenter
import javax.inject.Inject

class HomeViewPresenter : BasePresenter<HomeView>() {

  init {
    YapTalkerApp.appComponent.inject(this)
  }

  @Inject
  lateinit var yapNewsLoader: YapNewsLoader
}