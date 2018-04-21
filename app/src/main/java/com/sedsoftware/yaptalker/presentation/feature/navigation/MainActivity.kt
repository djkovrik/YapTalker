package com.sedsoftware.yaptalker.presentation.feature.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.sedsoftware.yaptalker.presentation.extensions.extractYapIds
import com.sedsoftware.yaptalker.presentation.extensions.getColorFromAttr
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel
import kotlinx.android.synthetic.main.activity_main_tablets.*
import kotlinx.android.synthetic.main.include_main_appbar.*
import ru.terrakok.cicerone.Navigator
import timber.log.Timber
import javax.inject.Inject

@LayoutResourceTablets(normalValue = R.layout.activity_main, tabletsValue = R.layout.activity_main_tablets)
class MainActivity : BaseActivity(), MainActivityView, NavigationView {

  companion object {
    private const val BOOKMARKS_ITEM_INSERT_POSITION = 4
  }

  @Inject
  lateinit var navigator: Navigator

  @Inject
  @InjectPresenter
  lateinit var presenter: MainActivityPresenter

  @ProvidePresenter
  fun provideMainPresenter(): MainActivityPresenter = presenter

  @Inject
  @InjectPresenter
  lateinit var navigationPresenter: NavigationPresenter

  @ProvidePresenter
  fun provideNavigationPresenter(): NavigationPresenter = navigationPresenter

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

  override fun setAppbarTitle(title: String) {
    supportActionBar?.title = title
  }

  override fun selectNavDrawerItem(item: Long) {
    navDrawer.setSelection(item, false)
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
        .withName(stringRes(R.string.nav_drawer_guest_name))
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

  @Suppress("PLUGIN_WARNING")
  private fun initializeNavigationDrawer(savedInstanceState: Bundle?) {

    drawerItemMainPage = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.MAIN_PAGE)
      .withName(R.string.nav_drawer_main_page)
      .withIcon(CommunityMaterial.Icon.cmd_home)
      .withTextColor(getColorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(getColorFromAttr(R.attr.colorNavMainPage))
      .withSelectedTextColor(getColorFromAttr(R.attr.colorNavMainPage))
      .withSelectedIconColor(getColorFromAttr(R.attr.colorNavMainPage))

    drawerItemForums = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.FORUMS)
      .withName(R.string.nav_drawer_forums)
      .withIcon(CommunityMaterial.Icon.cmd_forum)
      .withTextColor(getColorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(getColorFromAttr(R.attr.colorNavForums))
      .withSelectedTextColor(getColorFromAttr(R.attr.colorNavForums))
      .withSelectedIconColor(getColorFromAttr(R.attr.colorNavForums))

    drawerItemActiveTopics = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.ACTIVE_TOPICS)
      .withName(R.string.nav_drawer_active_topics)
      .withIcon(CommunityMaterial.Icon.cmd_bulletin_board)
      .withTextColor(getColorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(getColorFromAttr(R.attr.colorNavActiveTopics))
      .withSelectedTextColor(getColorFromAttr(R.attr.colorNavActiveTopics))
      .withSelectedIconColor(getColorFromAttr(R.attr.colorNavActiveTopics))

    drawerItemIncubator = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.INCUBATOR)
      .withName(R.string.nav_drawer_incubator)
      .withIcon(CommunityMaterial.Icon.cmd_human_child)
      .withTextColor(getColorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(getColorFromAttr(R.attr.colorNavIncubator))
      .withSelectedTextColor(getColorFromAttr(R.attr.colorNavIncubator))
      .withSelectedIconColor(getColorFromAttr(R.attr.colorNavIncubator))

    drawerItemBookmarks = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.BOOKMARKS)
      .withName(R.string.nav_drawer_bookmarks)
      .withIcon(CommunityMaterial.Icon.cmd_bookmark_outline)
      .withTextColor(getColorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(getColorFromAttr(R.attr.colorNavBookmarks))
      .withSelectedTextColor(getColorFromAttr(R.attr.colorNavBookmarks))
      .withSelectedIconColor(getColorFromAttr(R.attr.colorNavBookmarks))

    drawerItemSearch = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.SITE_SEARCH)
      .withName(R.string.nav_drawer_search)
      .withIcon(CommunityMaterial.Icon.cmd_magnify)
      .withTextColor(getColorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(getColorFromAttr(R.attr.colorNavSearch))
      .withSelectedTextColor(getColorFromAttr(R.attr.colorNavSearch))
      .withSelectedIconColor(getColorFromAttr(R.attr.colorNavSearch))

    drawerItemSettings = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.SETTINGS)
      .withIcon(CommunityMaterial.Icon.cmd_settings)
      .withName(R.string.nav_drawer_settings)
      .withTextColor(getColorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(getColorFromAttr(R.attr.colorNavSettings))
      .withSelectedTextColor(getColorFromAttr(R.attr.colorNavSettings))
      .withSelectedIconColor(getColorFromAttr(R.attr.colorNavSettings))

    drawerItemUpdater = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.APP_UPDATES)
      .withIcon(CommunityMaterial.Icon.cmd_download_network)
      .withName(R.string.nav_drawer_updates)
      .withTextColor(getColorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(getColorFromAttr(R.attr.colorNavSettings))
      .withSelectedTextColor(getColorFromAttr(R.attr.colorNavSettings))
      .withSelectedIconColor(getColorFromAttr(R.attr.colorNavSettings))

    drawerItemSignIn = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.SIGN_IN)
      .withName(R.string.nav_drawer_sign_in)
      .withIcon(CommunityMaterial.Icon.cmd_login)
      .withTextColor(getColorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(getColorFromAttr(R.attr.colorNavSignIn))
      .withSelectedTextColor(getColorFromAttr(R.attr.colorNavSignIn))
      .withSelectedIconColor(getColorFromAttr(R.attr.colorNavSignIn))

    drawerItemSignOut = PrimaryDrawerItem()
      .withIdentifier(NavigationSection.SIGN_OUT)
      .withName(R.string.nav_drawer_sign_out)
      .withIcon(CommunityMaterial.Icon.cmd_logout)
      .withTextColor(getColorFromAttr(R.attr.colorNavDefaultText))
      .withIconColor(getColorFromAttr(R.attr.colorNavSignIn))
      .withSelectedTextColor(getColorFromAttr(R.attr.colorNavSignIn))
      .withSelectedIconColor(getColorFromAttr(R.attr.colorNavSignIn))

    navHeader = AccountHeaderBuilder()
      .withActivity(this)
      .withHeaderBackground(R.drawable.bg_primary_solid)
      .withCompactStyle(true)
      .withSelectionListEnabledForSingleProfile(false)
      .withSavedInstance(savedInstanceState)
      .withOnAccountHeaderProfileImageListener(object : OnAccountHeaderProfileImageListener {
        override fun onProfileImageClick(view: View?, profile: IProfile<*>?, current: Boolean): Boolean {
          navigationPresenter.navigateToUserProfile()
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
          navigationPresenter.navigateToChosenSection(drawerItem.identifier)
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
        navigationPresenter.navigateWithIntentLink(navigateTo)
      }
    }
  }
}
