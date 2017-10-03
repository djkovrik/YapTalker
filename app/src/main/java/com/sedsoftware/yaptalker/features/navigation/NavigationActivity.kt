package com.sedsoftware.yaptalker.features.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.UpdateNavDrawerEvent
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastInfo
import com.sedsoftware.yaptalker.features.authorization.AuthorizationActivity
import com.sedsoftware.yaptalker.features.base.BaseActivity
import com.sedsoftware.yaptalker.features.forumslist.ForumsController
import com.sedsoftware.yaptalker.features.news.NewsController
import com.sedsoftware.yaptalker.features.settings.SettingsActivity
import kotlinx.android.synthetic.main.include_main_appbar.*
import kotlinx.android.synthetic.main.include_main_content.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult

class NavigationActivity : BaseActivity(), NavigationView {

  companion object {
    const val SIGN_IN_REQUEST = 1
  }

  @InjectPresenter
  lateinit var navigationViewPresenter: NavigationViewPresenter

  override val layoutId: Int
    get() = R.layout.activity_main

  override val contentFrame: ViewGroup
    get() = content_frame

  override val rootController: Controller
    get() = navigationViewPresenter.getFirstLaunchPage()

  private lateinit var navDrawer: Drawer
  private lateinit var navHeader: AccountHeader
  private lateinit var drawerItemMainPage: PrimaryDrawerItem
  private lateinit var drawerItemForums: PrimaryDrawerItem
  private lateinit var drawerItemSettings: PrimaryDrawerItem
  private lateinit var drawerItemSignIn: PrimaryDrawerItem
  private lateinit var drawerItemSignOut: PrimaryDrawerItem

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setSupportActionBar(toolbar)

    navigationViewPresenter.initLayout(savedInstanceState)

  }

  // Init Iconics here
  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(IconicsContextWrapper.wrap(base))
  }

  override fun onSaveInstanceState(outState: Bundle?) {
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

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    if (resultCode == Activity.RESULT_OK) {
      when (requestCode) {
        SIGN_IN_REQUEST -> navigationViewPresenter.refreshAuthorization()
      }
    }
  }

  override fun initDrawer(savedInstanceState: Bundle?) {

    drawerItemMainPage = PrimaryDrawerItem()
        .withIdentifier(Navigation.MAIN_PAGE)
        .withName(R.string.nav_drawer_main_page)
        .withIcon(CommunityMaterial.Icon.cmd_home)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavMainPage)
        .withSelectedTextColor(color(R.color.colorNavMainPage))
        .withSelectedIconColorRes(R.color.colorNavMainPage)

    drawerItemForums = PrimaryDrawerItem()
        .withIdentifier(Navigation.FORUMS)
        .withName(R.string.nav_drawer_forums)
        .withIcon(CommunityMaterial.Icon.cmd_forum)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavForums)
        .withSelectedTextColor(color(R.color.colorNavForums))
        .withSelectedIconColorRes(R.color.colorNavForums)

    drawerItemSettings = PrimaryDrawerItem()
        .withIdentifier(Navigation.SETTINGS)
        .withIcon(CommunityMaterial.Icon.cmd_settings)
        .withName(R.string.nav_drawer_settings)
        .withSelectable(false)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavSettings)
        .withSelectedTextColor(color(R.color.colorNavSettings))
        .withSelectedIconColorRes(R.color.colorNavSettings)

    drawerItemSignIn = PrimaryDrawerItem()
        .withIdentifier(Navigation.SIGN_IN)
        .withName(R.string.nav_drawer_sign_in)
        .withSelectable(false)
        .withIcon(CommunityMaterial.Icon.cmd_login)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavSignin)
        .withSelectedTextColor(color(R.color.colorNavSignin))
        .withSelectedIconColorRes(R.color.colorNavSignin)

    drawerItemSignOut = PrimaryDrawerItem()
        .withIdentifier(Navigation.SIGN_OUT)
        .withName(R.string.nav_drawer_sign_out)
        .withSelectable(false)
        .withIcon(CommunityMaterial.Icon.cmd_logout)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavSignin)
        .withSelectedTextColor(color(R.color.colorNavSignin))
        .withSelectedIconColorRes(R.color.colorNavSignin)

    navHeader = AccountHeaderBuilder()
        .withActivity(this)
        .withHeaderBackground(R.drawable.nav_header)
        .withCompactStyle(true)
        .withSelectionListEnabledForSingleProfile(false)
        .withSavedInstance(savedInstanceState)
        .build()

    navDrawer = DrawerBuilder()
        .withActivity(this)
        .withToolbar(toolbar)
        .withAccountHeader(navHeader)
        .addDrawerItems(drawerItemMainPage)
        .addDrawerItems(drawerItemForums)
        .addDrawerItems(DividerDrawerItem())
        .addDrawerItems(drawerItemSettings)
        .withOnDrawerItemClickListener { _, _, drawerItem ->
          if (drawerItem is Nameable<*>) {
            navigationViewPresenter.onNavigationClicked(drawerItem.identifier)
          }
          false
        }
        .withSavedInstance(savedInstanceState)
        .build()
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
      Navigation.SIGN_IN -> {
        startActivityForResult<AuthorizationActivity>(SIGN_IN_REQUEST)
      }
      Navigation.SIGN_OUT -> {
        navigationViewPresenter.signOut()
        navigationViewPresenter.refreshAuthorization()
        router.popToRoot()
      }
    }
  }

  override fun setActiveProfile(event: UpdateNavDrawerEvent) {

    val profile = if (event.name.isNotEmpty()) {
      ProfileDrawerItem()
          .withName(event.name)
          .withEmail(event.title)
          .withIcon(event.avatar)
          .withIdentifier(2L)
    } else {
      ProfileDrawerItem()
          .withName(stringRes(R.string.nav_drawer_guest_name))
          .withEmail("")
          .withIdentifier(1L)
    }

    navHeader.profiles.clear()
    navHeader.addProfiles(profile)

    if (navHeader.activeProfile.name.toString() == stringRes(R.string.nav_drawer_guest_name)) {
      signInItemAvailable()
    } else {
      signOutItemAvailable()
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

    navigationViewPresenter.refreshAuthorization()
  }

  override fun showSignOutMessage() {
    toastInfo(stringRes(R.string.msg_sign_out))
  }

  override fun goToMainPage() {
    router.popToRoot()
  }

  private fun signInItemAvailable() {
    navDrawer.removeItem(Navigation.SIGN_IN)
    navDrawer.removeItem(Navigation.SIGN_OUT)
    navDrawer.addItem(drawerItemSignIn)
  }

  private fun signOutItemAvailable() {
    navDrawer.removeItem(Navigation.SIGN_IN)
    navDrawer.removeItem(Navigation.SIGN_OUT)
    navDrawer.addItem(drawerItemSignOut)
  }
}
