package com.sedsoftware.yaptalker.features.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView.ScaleType.CENTER_CROP
import com.arellomobile.mvp.presenter.InjectPresenter
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
import com.sedsoftware.yaptalker.base.BaseActivity
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastInfo
import com.sedsoftware.yaptalker.commons.extensions.validateURL
import com.sedsoftware.yaptalker.data.model.AuthorizedUserInfo
import kotlinx.android.synthetic.main.include_main_appbar.*


class NavigationActivity : BaseActivity(), NavigationView {

  companion object {
    private const val APPBAR_TITLE_KEY = "APPBAR_TITLE_KEY"
    private const val SIGN_IN_REQUEST = 123
  }

  @InjectPresenter
  lateinit var navigationViewPresenter: NavigationViewPresenter

  override val layoutId: Int
    get() = R.layout.activity_main

  private lateinit var navDrawer: Drawer
  private lateinit var navHeader: AccountHeader
  private lateinit var drawerItemMainPage: PrimaryDrawerItem
  private lateinit var drawerItemForums: PrimaryDrawerItem
  private lateinit var drawerItemSettings: PrimaryDrawerItem
  private lateinit var drawerItemSignIn: PrimaryDrawerItem
  private lateinit var drawerItemSignOut: PrimaryDrawerItem

  private var isSignInAvailable = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setSupportActionBar(toolbar)

    navigationViewPresenter.initLayout(savedInstanceState)
    navigationViewPresenter.refreshAuthorization()
    navigationViewPresenter.restoreCurrentTitle(APPBAR_TITLE_KEY, savedInstanceState)

    handleLinkIntent()
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent)
    handleLinkIntent()
  }

  // Init Iconics here
  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(IconicsContextWrapper.wrap(base))
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    navigationViewPresenter.saveCurrentTitle(APPBAR_TITLE_KEY, outState)
    navDrawer.saveInstanceState(outState)
    navHeader.saveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    if (resultCode == Activity.RESULT_OK && requestCode == SIGN_IN_REQUEST) {
      navigationViewPresenter.refreshAuthorization()
    }
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
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
        .withHeaderBackground(R.drawable.nav_header_simple)
        .withHeaderBackgroundScaleType(CENTER_CROP)
        .withCompactStyle(true)
        .withSelectionListEnabledForSingleProfile(false)
        .withSavedInstance(savedInstanceState)
        .build()

    navDrawer = DrawerBuilder()
        .withActivity(this)
        .withAccountHeader(navHeader)
        .withToolbar(toolbar)
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

  }

  override fun showSignOutMessage() {
    toastInfo(stringRes(R.string.msg_sign_out))
  }

  override fun goToMainPage() {

  }

  override fun setAppbarTitle(title: String) {
    supportActionBar?.title = title
  }

  override fun updateNavDrawer(userInfo: AuthorizedUserInfo) {

    isSignInAvailable = userInfo.nickname.isEmpty()

    val profile = if (userInfo.nickname.isNotEmpty()) {
      ProfileDrawerItem()
          .withName(userInfo.nickname)
          .withEmail(userInfo.title)
          .withIcon(userInfo.avatar.validateURL())
          .withIdentifier(1L)
    } else {
      ProfileDrawerItem()
          .withName(stringRes(R.string.nav_drawer_guest_name))
          .withEmail("")
          .withIdentifier(2L)
    }

    navHeader.profiles.clear()
    navHeader.addProfiles(profile)

    when {
      isSignInAvailable -> displaySignIn()
      !isSignInAvailable -> displaySignOut()
    }
  }

  private fun displaySignIn() {
    navDrawer.removeItem(Navigation.SIGN_IN)
    navDrawer.removeItem(Navigation.SIGN_OUT)
    navDrawer.addItem(drawerItemSignIn)
  }

  private fun displaySignOut() {
    navDrawer.removeItem(Navigation.SIGN_IN)
    navDrawer.removeItem(Navigation.SIGN_OUT)
    navDrawer.addItem(drawerItemSignOut)
  }

  private fun handleLinkIntent() {
    val appLinkIntent = intent
    val appLinkAction = appLinkIntent.action
    val appLinkData = appLinkIntent.data

//    if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {
//      val regex = Pattern.compile("yaplakal.com/forum(\\d+)/topic(\\d+)\\.html")
//      val matcher = regex.matcher(appLinkData.toString())
//
//      if (matcher.find()) {
//        val bundle = Bundle()
//        bundle.putInt(ChosenTopicController.FORUM_ID_KEY, matcher.group(1).toInt())
//        bundle.putInt(ChosenTopicController.TOPIC_ID_KEY, matcher.group(2).toInt())
//
//        router.pushController(
//            RouterTransaction.with(ChosenTopicController(bundle))
//                .pushChangeHandler(FadeChangeHandler())
//                .popChangeHandler(FadeChangeHandler()))
//      }
//    }
  }
}
