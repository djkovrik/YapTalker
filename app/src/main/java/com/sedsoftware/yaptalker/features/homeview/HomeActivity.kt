package com.sedsoftware.yaptalker.features.homeview

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R

class HomeActivity : MvpAppCompatActivity(), HomeView {

  @InjectPresenter
  lateinit var homeViewPresenter: HomeViewPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun initNavigationDrawer() {

  }
}
