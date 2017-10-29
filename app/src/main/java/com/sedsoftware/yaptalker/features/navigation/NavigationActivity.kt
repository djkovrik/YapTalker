package com.sedsoftware.yaptalker.features.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.sedsoftware.yaptalker.features.NavigationScreens
import com.sedsoftware.yaptalker.features.authorization.AuthorizationFragment
import com.sedsoftware.yaptalker.features.forum.ChosenForumFragment
import com.sedsoftware.yaptalker.features.forumslist.ForumsFragment
import com.sedsoftware.yaptalker.features.news.NewsFragment
import com.sedsoftware.yaptalker.features.posting.AddMessageFragment
import com.sedsoftware.yaptalker.features.settings.SettingsActivity
import com.sedsoftware.yaptalker.features.topic.ChosenTopicFragment
import com.sedsoftware.yaptalker.features.userprofile.UserProfileFragment
import kotlinx.android.synthetic.main.include_main_appbar.*
import ru.terrakok.cicerone.android.SupportAppNavigator

class NavigationActivity : BaseActivity(), NavigationView {

  companion object {
    const val SIGN_IN_REQUEST = 123
    private const val APPBAR_TITLE_KEY = "APPBAR_TITLE_KEY"
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

  private val navigator = object : SupportAppNavigator(this, supportFragmentManager, R.id.content_frame) {

    override fun createActivityIntent(screenKey: String?, data: Any?): Intent? = when (screenKey) {
      NavigationScreens.SETTINGS_SCREEN -> SettingsActivity.getIntent(this@NavigationActivity)
      else -> null
    }

    override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
      NavigationScreens.NEWS_SCREEN -> NewsFragment.getNewInstance()
      NavigationScreens.FORUMS_LIST_SCREEN -> ForumsFragment.getNewInstance()
      NavigationScreens.CHOSEN_FORUM_SCREEN -> ChosenForumFragment.getNewInstance(data as Int)
      NavigationScreens.CHOSEN_TOPIC_SCREEN -> ChosenTopicFragment.getNewInstance(data as Pair<Int, Int>)
      NavigationScreens.USER_PROFILE_SCREEN -> UserProfileFragment.getNewInstance(data as Int)
      NavigationScreens.AUTHORIZATION_SCREEN -> AuthorizationFragment.getNewInstance()
      NavigationScreens.ADD_MESSAGE_SCREEN -> AddMessageFragment.getNewInstance(data as String)
      else -> null
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setSupportActionBar(toolbar)

    navigationViewPresenter.initLayout(savedInstanceState)
    navigationViewPresenter.refreshAuthorization()
    navigationViewPresenter.restoreCurrentTitle(APPBAR_TITLE_KEY, savedInstanceState)
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

  override fun onPause() {
    super.onPause()
    navigatorHolder.removeNavigator()
  }

  override fun onResume() {
    super.onResume()
    navigatorHolder.setNavigator(navigator)
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun initDrawer(savedInstanceState: Bundle?) {

    drawerItemMainPage = PrimaryDrawerItem()
        .withIdentifier(NavigationDrawerItems.MAIN_PAGE)
        .withName(R.string.nav_drawer_main_page)
        .withIcon(CommunityMaterial.Icon.cmd_home)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavMainPage)
        .withSelectedTextColor(color(R.color.colorNavMainPage))
        .withSelectedIconColorRes(R.color.colorNavMainPage)

    drawerItemForums = PrimaryDrawerItem()
        .withIdentifier(NavigationDrawerItems.FORUMS)
        .withName(R.string.nav_drawer_forums)
        .withIcon(CommunityMaterial.Icon.cmd_forum)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavForums)
        .withSelectedTextColor(color(R.color.colorNavForums))
        .withSelectedIconColorRes(R.color.colorNavForums)

    drawerItemSettings = PrimaryDrawerItem()
        .withIdentifier(NavigationDrawerItems.SETTINGS)
        .withIcon(CommunityMaterial.Icon.cmd_settings)
        .withName(R.string.nav_drawer_settings)
        .withSelectable(false)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavSettings)
        .withSelectedTextColor(color(R.color.colorNavSettings))
        .withSelectedIconColorRes(R.color.colorNavSettings)

    drawerItemSignIn = PrimaryDrawerItem()
        .withIdentifier(NavigationDrawerItems.SIGN_IN)
        .withName(R.string.nav_drawer_sign_in)
        .withIcon(CommunityMaterial.Icon.cmd_login)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavSignin)
        .withSelectedTextColor(color(R.color.colorNavSignin))
        .withSelectedIconColorRes(R.color.colorNavSignin)

    drawerItemSignOut = PrimaryDrawerItem()
        .withIdentifier(NavigationDrawerItems.SIGN_OUT)
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
            navigationViewPresenter.onNavigationDrawerClicked(drawerItem.identifier)
          }
          false
        }
        .withSavedInstance(savedInstanceState)
        .build()
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

  override fun setAppbarTitle(title: String) {
    supportActionBar?.title = title
  }

  override fun showSignOutMessage() {
    toastInfo(stringRes(R.string.msg_sign_out))
  }

  private fun displaySignIn() {
    navDrawer.removeItem(NavigationDrawerItems.SIGN_IN)
    navDrawer.removeItem(NavigationDrawerItems.SIGN_OUT)
    navDrawer.addItem(drawerItemSignIn)
  }

  private fun displaySignOut() {
    navDrawer.removeItem(NavigationDrawerItems.SIGN_IN)
    navDrawer.removeItem(NavigationDrawerItems.SIGN_OUT)
    navDrawer.addItem(drawerItemSignOut)
  }
}
