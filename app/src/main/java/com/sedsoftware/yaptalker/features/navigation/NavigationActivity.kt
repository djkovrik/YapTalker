package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.features.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_appbar.*

class NavigationActivity : BaseActivity(), NavigationView {

  @InjectPresenter
  lateinit var homeViewPresenter: NavigationViewPresenter

  private lateinit var drawer: Drawer

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    val drawerItem1 = PrimaryDrawerItem().withName("Main page")
    val drawerBuilder = DrawerBuilder()
        .withActivity(this)
        .withToolbar(toolbar)
        .addDrawerItems(drawerItem1)

    val twoPaneMode = resources.getBoolean(R.bool.two_pane_layout)

    if (twoPaneMode) {
      drawer = drawerBuilder.buildView()
      navigation_drawer.addView(drawer.slider)
    } else {
      drawer = drawerBuilder.build()
    }
  }
}
