package com.sedsoftware.yaptalker.presentation.feature.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeader.OnAccountHeaderProfileImageListener
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.model.interfaces.Nameable
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResourceTablets
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.colorFromAttr
import com.sedsoftware.yaptalker.presentation.extensions.extractYapIds
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel
import com.sedsoftware.yaptalker.presentation.provider.ActionBarProvider
import com.sedsoftware.yaptalker.presentation.provider.NavDrawerProvider
import kotlinx.android.synthetic.main.activity_main_tablets.*
import kotlinx.android.synthetic.main.include_main_appbar.*
import ru.terrakok.cicerone.Navigator
import timber.log.Timber
import javax.inject.Inject

@LayoutResourceTablets(normalValue = R.layout.activity_main, tabletsValue = R.layout.activity_main_tablets)
class MainActivity : BaseActivity(), MainActivityView, ActionBarProvider, NavDrawerProvider {

  companion object {
    private const val BOOKMARKS_ITEM_INSERT_POSITION = 4
  }

  @Inject
  lateinit var navigator: Navigator

  @Inject
  @InjectPresenter
  lateinit var presenter: MainActivityPresenter

  @ProvidePresenter
  fun providePresenter(): MainActivityPresenter = presenter

  private val isInTwoPaneMode: Boolean by lazy {
    settings.isInTwoPaneMode()
  }

  private lateinit var navDrawer: Drawer
  private lateinit var navHeader: AccountHeader
  private lateinit var drawerItemMainPage: PrimaryDrawerItem
  private lateinit var drawerItemForums: PrimaryDrawerItem
  private lateinit var drawerItemActiveTopics: PrimaryDrawerItem
  private lateinit var drawerItemIncubator: PrimaryDrawerItem
  private lateinit var drawerItemBookmarks: PrimaryDrawerItem
  private lateinit var drawerItemSearch: PrimaryDrawerItem
  private lateinit var drawerItemSettings: PrimaryDrawerItem
  private lateinit var drawerItemUpdater: PrimaryDrawerItem
  private lateinit var drawerItemSignIn: PrimaryDrawerItem
  private lateinit var drawerItemSignOut: PrimaryDrawerItem

  // Init Iconics here
  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(IconicsContextWrapper.wrap(base))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    initializeNavigationDrawer(savedInstanceState)
    handleLinkIntent()
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent)
    handleLinkIntent()
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
      !isInTwoPaneMode && navDrawer.isDrawerOpen -> navDrawer.closeDrawer()
      backPressFragment.onBackPressed() -> Timber.i("Back press event consumed by fragment.")
      else -> super.onBackPressed()
    }
  }

  override fun updateNavDrawerProfile(userInfo: LoginSessionInfoModel) {
    val profile = if (userInfo.nickname.isNotEmpty()) {
      ProfileDrawerItem()
        .withName(userInfo.nickname)
        .withEmail(userInfo.title)
        .withIcon(userInfo.avatar.validateUrl())
        .withIdentifier(1L)
    } else {
      ProfileDrawerItem()
        .withName(string(R.string.nav_drawer_guest_name))
        .withEmail("")
        .withIdentifier(2L)
    }

    navHeader.profiles.clear()
    navHeader.addProfiles(profile)
  }

  override fun clearDynamicNavigationItems() {
    navDrawer.removeItem(NavigationSection.SIGN_IN)
    navDrawer.removeItem(NavigationSection.SIGN_OUT)
    navDrawer.removeItem(NavigationSection.BOOKMARKS)
  }

  override fun displaySignedInNavigation() {
    navDrawer.addItemAtPosition(drawerItemBookmarks, BOOKMARKS_ITEM_INSERT_POSITION)
    navDrawer.addItem(drawerItemSignOut)
  }

  override fun displaySignedOutNavigation() {
    navDrawer.addItem(drawerItemSignIn)
  }

  override fun closeNavigationDrawer() {
    navDrawer.closeDrawer()
  }

  override fun getCurrentActionBar(): ActionBar? = supportActionBar

  override fun getCurrentDrawer(): Drawer = navDrawer

  @Suppress("PLUGIN_WARNING")
  private fun initializeNavigationDrawer(savedInstanceState: Bundle?) {

    drawerItemMainPage = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.MAIN_PAGE)
      .withName(R.string.nav_drawer_main_page)
      .withIcon(CommunityMaterial.Icon.cmd_home)
      .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(colorFromAttr(R.attr.colorNavMainPage))
      .withSelectedTextColor(colorFromAttr(R.attr.colorNavMainPage))
      .withSelectedIconColor(colorFromAttr(R.attr.colorNavMainPage))

    drawerItemForums = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.FORUMS)
      .withName(R.string.nav_drawer_forums)
      .withIcon(CommunityMaterial.Icon.cmd_forum)
      .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(colorFromAttr(R.attr.colorNavForums))
      .withSelectedTextColor(colorFromAttr(R.attr.colorNavForums))
      .withSelectedIconColor(colorFromAttr(R.attr.colorNavForums))

    drawerItemActiveTopics = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.ACTIVE_TOPICS)
      .withName(R.string.nav_drawer_active_topics)
      .withIcon(CommunityMaterial.Icon.cmd_bulletin_board)
      .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(colorFromAttr(R.attr.colorNavActiveTopics))
      .withSelectedTextColor(colorFromAttr(R.attr.colorNavActiveTopics))
      .withSelectedIconColor(colorFromAttr(R.attr.colorNavActiveTopics))

    drawerItemIncubator = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.INCUBATOR)
      .withName(R.string.nav_drawer_incubator)
      .withIcon(CommunityMaterial.Icon.cmd_human_child)
      .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(colorFromAttr(R.attr.colorNavIncubator))
      .withSelectedTextColor(colorFromAttr(R.attr.colorNavIncubator))
      .withSelectedIconColor(colorFromAttr(R.attr.colorNavIncubator))

    drawerItemBookmarks = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.BOOKMARKS)
      .withName(R.string.nav_drawer_bookmarks)
      .withIcon(CommunityMaterial.Icon.cmd_bookmark_outline)
      .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(colorFromAttr(R.attr.colorNavBookmarks))
      .withSelectedTextColor(colorFromAttr(R.attr.colorNavBookmarks))
      .withSelectedIconColor(colorFromAttr(R.attr.colorNavBookmarks))

    drawerItemSearch = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.SITE_SEARCH)
      .withName(R.string.nav_drawer_search)
      .withIcon(CommunityMaterial.Icon.cmd_magnify)
      .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(colorFromAttr(R.attr.colorNavSearch))
      .withSelectedTextColor(colorFromAttr(R.attr.colorNavSearch))
      .withSelectedIconColor(colorFromAttr(R.attr.colorNavSearch))

    drawerItemSettings = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.SETTINGS)
      .withIcon(CommunityMaterial.Icon.cmd_settings)
      .withName(R.string.nav_drawer_settings)
      .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(colorFromAttr(R.attr.colorNavSettings))
      .withSelectedTextColor(colorFromAttr(R.attr.colorNavSettings))
      .withSelectedIconColor(colorFromAttr(R.attr.colorNavSettings))

    drawerItemUpdater = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.APP_UPDATES)
      .withIcon(CommunityMaterial.Icon.cmd_download_network)
      .withName(R.string.nav_drawer_updates)
      .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(colorFromAttr(R.attr.colorNavSettings))
      .withSelectedTextColor(colorFromAttr(R.attr.colorNavSettings))
      .withSelectedIconColor(colorFromAttr(R.attr.colorNavSettings))

    drawerItemSignIn = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.SIGN_IN)
      .withName(R.string.nav_drawer_sign_in)
      .withIcon(CommunityMaterial.Icon.cmd_login)
      .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(colorFromAttr(R.attr.colorNavSignIn))
      .withSelectedTextColor(colorFromAttr(R.attr.colorNavSignIn))
      .withSelectedIconColor(colorFromAttr(R.attr.colorNavSignIn))

    drawerItemSignOut = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.SIGN_OUT)
      .withName(R.string.nav_drawer_sign_out)
      .withIcon(CommunityMaterial.Icon.cmd_logout)
      .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(colorFromAttr(R.attr.colorNavSignIn))
      .withSelectedTextColor(colorFromAttr(R.attr.colorNavSignIn))
      .withSelectedIconColor(colorFromAttr(R.attr.colorNavSignIn))

    navHeader = AccountHeaderBuilder()
      .withActivity(this)
      .withHeaderBackground(R.drawable.bg_primary_solid)
      .withCompactStyle(true)
      .withSelectionListEnabledForSingleProfile(false)
      .withSavedInstance(savedInstanceState)
      .withOnAccountHeaderProfileImageListener(object : OnAccountHeaderProfileImageListener {
        override fun onProfileImageClick(view: View?, profile: IProfile<*>?, current: Boolean): Boolean {
          presenter.navigateToUserProfile()
          return true
        }

        override fun onProfileImageLongClick(view: View?, profile: IProfile<*>?, current: Boolean) = false
      })
      .build()

    val drawerBuilder = DrawerBuilder()
      .withActivity(this)
      .withAccountHeader(navHeader)
      .withToolbar(toolbar)
      .addDrawerItems(drawerItemMainPage)
      .addDrawerItems(drawerItemForums)
      .addDrawerItems(drawerItemActiveTopics)
      .addDrawerItems(drawerItemIncubator)
      .addDrawerItems(drawerItemSearch)
      .addDrawerItems(DividerDrawerItem())
      .addDrawerItems(drawerItemSettings)
      .addDrawerItems(drawerItemUpdater)
      .withOnDrawerItemClickListener { _, _, drawerItem ->
        if (drawerItem is Nameable<*>) {
          presenter.navigateToChosenSection(drawerItem.identifier)
        }
        false
      }
      .withSavedInstance(savedInstanceState)

    if (isInTwoPaneMode) {
      navDrawer = drawerBuilder.buildView()
      navigation_drawer.addView(navDrawer.slider)
    } else {
      navDrawer = drawerBuilder.build()
    }
  }

  private fun handleLinkIntent() {
    val appLinkIntent = intent
    val appLinkAction = appLinkIntent.action
    val appLinkData = appLinkIntent.data

    if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {

      val navigateTo = appLinkData.toString().extractYapIds()

      if (navigateTo.first != 0) {
        presenter.navigateWithIntentLink(navigateTo)
      }
    }
  }
}
