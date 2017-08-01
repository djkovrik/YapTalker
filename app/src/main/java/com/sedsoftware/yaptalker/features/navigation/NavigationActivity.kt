package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R

class NavigationActivity : MvpAppCompatActivity(), NavigationView {

  @InjectPresenter
  lateinit var homeViewPresenter: NavigationViewPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun initNavigationDrawer() {

  }
}
