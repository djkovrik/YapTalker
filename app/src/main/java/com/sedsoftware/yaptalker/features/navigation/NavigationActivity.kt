package com.sedsoftware.yaptalker.features.navigation

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.features.base.BaseActivity
import com.sedsoftware.yaptalker.features.forumslist.ForumsController
import com.sedsoftware.yaptalker.features.news.NewsController
import com.sedsoftware.yaptalker.features.settings.SettingsActivity
import kotlinx.android.synthetic.main.include_main_appbar.*
import kotlinx.android.synthetic.main.include_main_content.*
import org.jetbrains.anko.startActivity

class NavigationActivity : BaseActivity(), NavigationView {

  @InjectPresenter
  lateinit var navigationViewPresenter: NavigationViewPresenter

  override val layoutId: Int
    get() = R.layout.activity_main

  override val contentFrame: ViewGroup
    get() = content_frame

  override val rootController: Controller
    get() = navigationViewPresenter.getFirstLaunchPage()

  // Navigation navDrawer contents
  private lateinit var navDrawer: Drawer
  private lateinit var navHeader: AccountHeader

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setSupportActionBar(toolbar)

    navigationViewPresenter.initLayout(savedInstanceState)
  }

  // Init Iconics here
  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(IconicsContextWrapper.wrap(base))
  }

  override fun onSaveInstanceState(outState: Bundle) {
    navDrawer.saveInstanceState(outState)
    navHeader.saveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }

  override fun onBackPressed() {
    when {
      navDrawer.isDrawerOpen -> navDrawer.closeDrawer()
      !router.handleBack() -> super.onBackPressed()
    }
  }

  override fun initDrawer(savedInstanceState: Bundle?) {

    navDrawer = drawer {

      toolbar = this@NavigationActivity.toolbar
      savedInstance = savedInstanceState
      selectedItem = Navigation.MAIN_PAGE
      hasStableIds = true

      navHeader = accountHeader {

        savedInstance = savedInstanceState
        translucentStatusBar = true
        background = R.drawable.nav_header
        selectionListEnabledForSingleProfile = false

        profile {
          name = getString(R.string.nav_drawer_guest_name)
        }
      }

      onItemClick { _, _, drawerItem ->
        if (drawerItem is Nameable<*>) {
          navigationViewPresenter.onNavigationClicked(drawerItem.identifier)
        }
        false
      }

      primaryItem {
        identifier = Navigation.MAIN_PAGE
        name = stringRes(R.string.nav_drawer_main_page)
        iicon = CommunityMaterial.Icon.cmd_home
        textColor = color(R.color.colorNavDefaultText).toLong()
        iconColorRes = R.color.colorNavMainPage
        selectedTextColor = color(R.color.colorNavMainPage).toLong()
        selectedIconColorRes = R.color.colorNavMainPage
      }

      primaryItem {
        identifier = Navigation.FORUMS
        name = stringRes(R.string.nav_drawer_forums)
        iicon = CommunityMaterial.Icon.cmd_forum
        textColor = color(R.color.colorNavDefaultText).toLong()
        iconColorRes = R.color.colorNavForums
        selectedTextColor = color(R.color.colorNavForums).toLong()
        selectedIconColorRes = R.color.colorNavForums
      }

      divider()

      primaryItem {
        identifier = Navigation.SETTINGS
        name = stringRes(R.string.nav_drawer_settings)
        selectable = false
        iicon = CommunityMaterial.Icon.cmd_settings
        textColor = color(R.color.colorNavDefaultText).toLong()
        iconColorRes = R.color.colorNavSettings
        selectedTextColor = color(R.color.colorNavSettings).toLong()
        selectedIconColorRes = R.color.colorNavSettings
      }

      primaryItem {
        identifier = Navigation.AUTHORIZATION
        name = stringRes(R.string.nav_drawer_sign_in)
        selectable = false
        iicon = CommunityMaterial.Icon.cmd_account
        textColor = color(R.color.colorNavDefaultText).toLong()
        iconColorRes = R.color.colorNavSignin
        selectedTextColor = color(R.color.colorNavSignin).toLong()
        selectedIconColorRes = R.color.colorNavSignin
      }
    }
  }

  override fun goToChosenSection(section: Long) {

    router.popToRoot()

    when (section) {
      Navigation.MAIN_PAGE -> {
        router.pushController(
            RouterTransaction.with(NewsController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
      }
      Navigation.FORUMS -> {
        router.pushController(
            RouterTransaction.with(ForumsController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler()))
      }
      Navigation.SETTINGS -> {
        startActivity<SettingsActivity>()
      }
      Navigation.AUTHORIZATION -> {
        navigationViewPresenter.loginAttempt()
      }
    }
  }

  override fun setAppbarTitle(text: String) {
    supportActionBar?.title = text
  }

  override fun onControllerChanged(target: Controller?) {
    when (target) {
      is NewsController -> navDrawer.setSelection(Navigation.MAIN_PAGE, false)
      else -> navDrawer.setSelection(Navigation.FORUMS, false)
    }
  }
}
