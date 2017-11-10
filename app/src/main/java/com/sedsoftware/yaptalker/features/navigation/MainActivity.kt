package com.sedsoftware.yaptalker.features.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.widget.ImageView.ScaleType
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
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
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.commons.extensions.extractYapIds
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.textFromHtml
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastInfo
import com.sedsoftware.yaptalker.commons.extensions.validateUrl
import com.sedsoftware.yaptalker.data.parsing.AuthorizedUserInfo
import com.sedsoftware.yaptalker.features.activetopics.ActiveTopicsFragment
import com.sedsoftware.yaptalker.features.authorization.AuthorizationFragment
import com.sedsoftware.yaptalker.features.bookmarks.BookmarksFragment
import com.sedsoftware.yaptalker.features.forum.ChosenForumFragment
import com.sedsoftware.yaptalker.features.forumslist.ForumsFragment
import com.sedsoftware.yaptalker.features.news.NewsFragment
import com.sedsoftware.yaptalker.features.posting.AddMessageFragment
import com.sedsoftware.yaptalker.features.settings.SettingsActivity
import com.sedsoftware.yaptalker.features.topic.ChosenTopicFragment
import com.sedsoftware.yaptalker.features.userprofile.UserProfileFragment
import kotlinx.android.synthetic.main.include_main_appbar.*
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

class MainActivity : BaseActivity(), MainActivityView, NavigationView {

  companion object {
    private const val BOOKMARKS_ITEM_INSERT_POSITION = 4
  }

  @InjectPresenter
  lateinit var mainActivityPresenter: MainActivityPresenter

  @InjectPresenter
  lateinit var navigationPresenter: NavigationPresenter

  override val layoutId: Int
    get() = R.layout.activity_main

  private lateinit var navDrawer: Drawer
  private lateinit var navHeader: AccountHeader
  private lateinit var drawerItemMainPage: PrimaryDrawerItem
  private lateinit var drawerItemForums: PrimaryDrawerItem
  private lateinit var drawerItemActiveTopics: PrimaryDrawerItem
  private lateinit var drawerItemBookmarks: PrimaryDrawerItem
  private lateinit var drawerItemSettings: PrimaryDrawerItem
  private lateinit var drawerItemSignIn: PrimaryDrawerItem
  private lateinit var drawerItemSignOut: PrimaryDrawerItem

  private val navigator = object : SupportAppNavigator(this, supportFragmentManager, R.id.content_frame) {

    override fun createActivityIntent(screenKey: String?, data: Any?): Intent? = when (screenKey) {
      NavigationScreens.SETTINGS_SCREEN -> SettingsActivity.getIntent(this@MainActivity)
      else -> null
    }

    @Suppress("UNCHECKED_CAST")
    override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
      NavigationScreens.NEWS_SCREEN -> NewsFragment.getNewInstance()
      NavigationScreens.FORUMS_LIST_SCREEN -> ForumsFragment.getNewInstance()
      NavigationScreens.CHOSEN_FORUM_SCREEN -> ChosenForumFragment.getNewInstance(data as Int)
      NavigationScreens.ACTIVE_TOPICS_SCREEN -> ActiveTopicsFragment.getNewInstance()
      NavigationScreens.BOOKMARKS_SCREEN -> BookmarksFragment.getNewInstance()
      NavigationScreens.CHOSEN_TOPIC_SCREEN -> ChosenTopicFragment.getNewInstance(data as Triple<Int, Int, Int>)
      NavigationScreens.USER_PROFILE_SCREEN -> UserProfileFragment.getNewInstance(data as Int)
      NavigationScreens.AUTHORIZATION_SCREEN -> AuthorizationFragment.getNewInstance()
      NavigationScreens.ADD_MESSAGE_SCREEN -> AddMessageFragment.getNewInstance(data as String)
      else -> null
    }

    override fun setupFragmentTransactionAnimation(command: Command?,
                                                   currentFragment: Fragment?,
                                                   nextFragment: Fragment?,
                                                   fragmentTransaction: FragmentTransaction?) {

      fragmentTransaction?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setSupportActionBar(toolbar)

    initNavDrawer(savedInstanceState)
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

  override fun onBackPressed() {
    when {
      navDrawer.isDrawerOpen -> navDrawer.closeDrawer()
      else -> super.onBackPressed()
    }
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun setAppbarTitle(title: String) {
    supportActionBar?.title = title
  }

  override fun showEula() {
    val dialog = MaterialDialog.Builder(this)
        .title(R.string.eula_title)
        .customView(R.layout.custom_view_eula, true)
        .positiveText(R.string.eula_button_ok)
        .onPositive { _, _ -> mainActivityPresenter.onEulaAccept() }
        .build()

    dialog.customView?.findViewById<TextView>(R.id.eula_text_view)?.textFromHtml(getString(R.string.eula_text))
    dialog.show()
  }

  override fun updateNavDrawerProfile(userInfo: AuthorizedUserInfo) {
    val profile = if (userInfo.nickname.isNotEmpty()) {
      ProfileDrawerItem()
          .withName(userInfo.nickname)
          .withEmail(userInfo.title)
          .withIcon(userInfo.avatar.validateUrl())
          .withIdentifier(1L)
    } else {
      ProfileDrawerItem()
          .withName(stringRes(R.string.nav_drawer_guest_name))
          .withEmail("")
          .withIdentifier(2L)
    }

    navHeader.profiles.clear()
    navHeader.addProfiles(profile)
  }

  override fun clearDynamicNavigationItems() {
    navDrawer.removeItem(NavigationDrawerItems.SIGN_IN)
    navDrawer.removeItem(NavigationDrawerItems.SIGN_OUT)
    navDrawer.removeItem(NavigationDrawerItems.BOOKMARKS)
  }

  override fun displaySignedInNavigation() {
    navDrawer.addItemAtPosition(drawerItemBookmarks, BOOKMARKS_ITEM_INSERT_POSITION)
    navDrawer.addItem(drawerItemSignOut)
  }

  override fun displaySignedOutNavigation() {
    navDrawer.addItem(drawerItemSignIn)
  }

  override fun showSignOutMessage() {
    toastInfo(stringRes(R.string.msg_sign_out))
  }

  private fun initNavDrawer(savedInstanceState: Bundle?) {

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

    drawerItemActiveTopics = PrimaryDrawerItem()
        .withIdentifier(NavigationDrawerItems.ACTIVE_TOPICS)
        .withName(R.string.nav_drawer_active_topics)
        .withIcon(CommunityMaterial.Icon.cmd_bulletin_board)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavActiveTopics)
        .withSelectedTextColor(color(R.color.colorNavActiveTopics))
        .withSelectedIconColorRes(R.color.colorNavActiveTopics)

    drawerItemBookmarks = PrimaryDrawerItem()
        .withIdentifier(NavigationDrawerItems.BOOKMARKS)
        .withName(R.string.nav_drawer_bookmarks)
        .withIcon(CommunityMaterial.Icon.cmd_bookmark_outline)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavBookmarks)
        .withSelectedTextColor(color(R.color.colorNavBookmarks))
        .withSelectedIconColorRes(R.color.colorNavBookmarks)

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
        .withSelectable(false)
        .withIcon(CommunityMaterial.Icon.cmd_login)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavSignIn)
        .withSelectedTextColor(color(R.color.colorNavSignIn))
        .withSelectedIconColorRes(R.color.colorNavSignIn)

    drawerItemSignOut = PrimaryDrawerItem()
        .withIdentifier(NavigationDrawerItems.SIGN_OUT)
        .withName(R.string.nav_drawer_sign_out)
        .withSelectable(false)
        .withIcon(CommunityMaterial.Icon.cmd_logout)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavSignIn)
        .withSelectedTextColor(color(R.color.colorNavSignIn))
        .withSelectedIconColorRes(R.color.colorNavSignIn)

    navHeader = AccountHeaderBuilder()
        .withActivity(this)
        .withHeaderBackground(R.drawable.nav_header_simple)
        .withHeaderBackgroundScaleType(ScaleType.CENTER_CROP)
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
        .addDrawerItems(drawerItemActiveTopics)
        .addDrawerItems(DividerDrawerItem())
        .addDrawerItems(drawerItemSettings)
        .withOnDrawerItemClickListener { _, _, drawerItem ->
          if (drawerItem is Nameable<*>) {
            navigationPresenter.onNavigationDrawerClicked(drawerItem.identifier)
          }
          false
        }
        .withSavedInstance(savedInstanceState)
        .build()
  }

  private fun handleLinkIntent() {
    val appLinkIntent = intent
    val appLinkAction = appLinkIntent.action
    val appLinkData = appLinkIntent.data

    if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {

      val navigateTo = appLinkData.toString().extractYapIds()

      if (navigateTo.first != 0) {
        mainActivityPresenter.navigateWithIntentLink(navigateTo)
      }
    }
  }
}
