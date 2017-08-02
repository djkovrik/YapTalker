package com.sedsoftware.yaptalker.features.navigation

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.evernote.android.state.State
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.booleanRes
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.features.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_appbar.*
import kotlinx.android.synthetic.main.activity_main_content.*


class NavigationActivity : BaseActivity(), NavigationView {

  @InjectPresenter
  lateinit var navigationViewPresenter: NavigationViewPresenter

  @State
  var currentNavigationItem = Navigation.MAIN_PAGE

  private lateinit var router: Router
  private val twoPaneMode by lazy { booleanRes(R.bool.two_pane_layout) }

  // Navigation drawer contents
  private lateinit var drawer: Drawer
  private lateinit var drawerItemMainPage: PrimaryDrawerItem
  private lateinit var drawerItemForums: PrimaryDrawerItem
  private lateinit var drawerItemSettings: PrimaryDrawerItem

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    navigationViewPresenter.initLayout(savedInstanceState)
  }

  override fun onBackPressed() {
    if (!router.handleBack()) {
      super.onBackPressed()
    }
  }

  override fun initDrawer() {

    drawerItemMainPage = PrimaryDrawerItem()
        .withIdentifier(Navigation.MAIN_PAGE)
        .withName(R.string.nav_drawer_main_page)
        .withIcon(GoogleMaterial.Icon.gmd_home)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavMainPage)
        .withSelectedTextColor(color(R.color.colorNavMainPage))
        .withSelectedIconColorRes(R.color.colorNavMainPage)

    drawerItemForums = PrimaryDrawerItem()
        .withIdentifier(Navigation.FORUMS)
        .withName(R.string.nav_drawer_forums)
        .withIcon(GoogleMaterial.Icon.gmd_forum)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavForums)
        .withSelectedTextColor(color(R.color.colorNavForums))
        .withSelectedIconColorRes(R.color.colorNavForums)

    drawerItemSettings = PrimaryDrawerItem()
        .withIdentifier(Navigation.SETTINGS)
        .withIcon(GoogleMaterial.Icon.gmd_settings)
        .withName(R.string.nav_drawer_settings)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavSettings)
        .withSelectedTextColor(color(R.color.colorNavSettings))
        .withSelectedIconColorRes(R.color.colorNavSettings)

    val drawerBuilder = DrawerBuilder()
        .withActivity(this)
        .withToolbar(toolbar)
        .addDrawerItems(drawerItemMainPage)
        .addDrawerItems(drawerItemForums)
        .addDrawerItems(DividerDrawerItem())
        .addDrawerItems(drawerItemSettings)
        .withSelectedItem(currentNavigationItem)
        .withOnDrawerItemClickListener { _, _, drawerItem ->
          if (drawerItem is Nameable<*>) {
            currentNavigationItem = drawerItem.identifier
            navigationViewPresenter.onNavigationClicked(currentNavigationItem)
          }
          false
        }

    if (twoPaneMode) {
      drawer = drawerBuilder.buildView()
      navigation_drawer.addView(drawer.slider)
    } else {
      drawer = drawerBuilder.build()
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
