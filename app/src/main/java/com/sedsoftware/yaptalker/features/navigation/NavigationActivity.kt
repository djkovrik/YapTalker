package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.booleanRes
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.features.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_appbar.*
import kotlinx.android.synthetic.main.activity_main_content.*


class NavigationActivity : BaseActivity(), NavigationView {

  @InjectPresenter
  lateinit var navigationViewPresenter: NavigationViewPresenter

  private lateinit var router: Router
  private val isInTwoPaneMode by lazy { booleanRes(R.bool.two_pane_layout) }

  // Navigation navDrawer contents
  private lateinit var navDrawer: Drawer
  private lateinit var drawerItemMainPage: PrimaryDrawerItem
  private lateinit var drawerItemForums: PrimaryDrawerItem
  private lateinit var drawerItemSettings: PrimaryDrawerItem

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    navigationViewPresenter.initLayout(savedInstanceState)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    navDrawer.saveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }

  override fun onBackPressed() {

    if (navDrawer.isDrawerOpen) {
      navDrawer.closeDrawer()
    } else if (!router.handleBack()) {
      super.onBackPressed()
    }
  }

  override fun initDrawer(savedInstanceState: Bundle?) {

    navDrawer = drawer {

      toolbar = this@NavigationActivity.toolbar
      savedInstance = savedInstanceState
      selectedItem = Navigation.MAIN_PAGE
      buildViewOnly = isInTwoPaneMode

      onItemClick { _, _, drawerItem ->
        if (drawerItem is Nameable<*>) {
          navigationViewPresenter.onNavigationClicked(drawerItem.identifier)
        }
        false
      }

      drawerItemMainPage = primaryItem {
        name = stringRes(R.string.nav_drawer_main_page)
        iicon = GoogleMaterial.Icon.gmd_home
        textColor = color(R.color.colorNavDefaultText).toLong()
        iconColorRes = R.color.colorNavMainPage
        selectedTextColor = color(R.color.colorNavMainPage).toLong()
        selectedIconColorRes = R.color.colorNavMainPage
      }

      drawerItemForums = primaryItem {
        identifier = Navigation.FORUMS
        name = stringRes(R.string.nav_drawer_forums)
        iicon = GoogleMaterial.Icon.gmd_forum
        textColor = color(R.color.colorNavDefaultText).toLong()
        iconColorRes = R.color.colorNavForums
        selectedTextColor = color(R.color.colorNavForums).toLong()
        selectedIconColorRes = R.color.colorNavForums
      }

      divider()

      drawerItemSettings = primaryItem {
        identifier = Navigation.SETTINGS
        name = stringRes(R.string.nav_drawer_settings)
        iicon = GoogleMaterial.Icon.gmd_settings
        textColor = color(R.color.colorNavDefaultText).toLong()
        iconColorRes = R.color.colorNavSettings
        selectedTextColor = color(R.color.colorNavSettings).toLong()
        selectedIconColorRes = R.color.colorNavSettings
      }
    }

    if (isInTwoPaneMode) {
      navigation_drawer.addView(navDrawer.slider)
    }
  }

  override fun initRouter(savedInstanceState: Bundle?) {
    router = Conductor.attachRouter(this, content_frame, savedInstanceState)
//    if (!router.hasRootController()) {
//      router.setRoot(RouterTransaction.with())
//    }
  }

  override fun goToChosenSection(section: Long) {
    // TODO() Switch controllers here
  }
}
