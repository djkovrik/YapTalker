package com.sedsoftware.yaptalker.presentation.features.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spanned
import android.widget.ImageView.ScaleType
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
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
import com.sedsoftware.yaptalker.commons.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.extensions.booleanRes
import com.sedsoftware.yaptalker.presentation.extensions.color
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.extensions.toastInfo
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_main_appbar.*
import ru.terrakok.cicerone.Navigator
import javax.inject.Inject

class MainActivity : BaseActivity(), MainActivityView, NavigationView {

  companion object {
    private const val BOOKMARKS_ITEM_INSERT_POSITION = 4
  }

  override val layoutId: Int
    get() = R.layout.activity_main

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
    booleanRes(R.bool.two_pane_mode)
  }

  private lateinit var navDrawer: Drawer
  private lateinit var navHeader: AccountHeader
  private lateinit var drawerItemMainPage: PrimaryDrawerItem
  private lateinit var drawerItemForums: PrimaryDrawerItem
  private lateinit var drawerItemActiveTopics: PrimaryDrawerItem
  private lateinit var drawerItemBookmarks: PrimaryDrawerItem
  private lateinit var drawerItemSettings: PrimaryDrawerItem
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
      else -> super.onBackPressed()
    }
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun setAppbarTitle(title: String) {
    supportActionBar?.title = title
  }

  override fun selectNavDrawerItem(item: Long) {
    navDrawer.setSelection(item, false)
  }

  override fun displayFormattedEulaText(spanned: Spanned) {
    val dialog = MaterialDialog.Builder(this)
        .title(R.string.eula_title)
        .customView(R.layout.custom_view_eula, true)
        .positiveText(R.string.eula_button_ok)
        .onPositive { _, _ -> presenter.markEulaAsAccepted() }
        .build()

    dialog.customView?.findViewById<TextView>(R.id.eula_text_view)?.text = spanned
    dialog.show()
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

  override fun showSignOutMessage() {
    toastInfo(stringRes(R.string.msg_sign_out))
  }

  @Suppress("PLUGIN_WARNING")
  private fun initializeNavigationDrawer(savedInstanceState: Bundle?) {

    drawerItemMainPage = PrimaryDrawerItem()
        .withIdentifier(NavigationSection.MAIN_PAGE)
        .withName(R.string.nav_drawer_main_page)
        .withSelectable(false)
        .withIcon(CommunityMaterial.Icon.cmd_home)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavMainPage)
        .withSelectedTextColor(color(R.color.colorNavMainPage))
        .withSelectedIconColorRes(R.color.colorNavMainPage)

    drawerItemForums = PrimaryDrawerItem()
        .withIdentifier(NavigationSection.FORUMS)
        .withName(R.string.nav_drawer_forums)
        .withSelectable(false)
        .withIcon(CommunityMaterial.Icon.cmd_forum)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavForums)
        .withSelectedTextColor(color(R.color.colorNavForums))
        .withSelectedIconColorRes(R.color.colorNavForums)

    drawerItemActiveTopics = PrimaryDrawerItem()
        .withIdentifier(NavigationSection.ACTIVE_TOPICS)
        .withName(R.string.nav_drawer_active_topics)
        .withSelectable(false)
        .withIcon(CommunityMaterial.Icon.cmd_bulletin_board)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavActiveTopics)
        .withSelectedTextColor(color(R.color.colorNavActiveTopics))
        .withSelectedIconColorRes(R.color.colorNavActiveTopics)

    drawerItemBookmarks = PrimaryDrawerItem()
        .withIdentifier(NavigationSection.BOOKMARKS)
        .withName(R.string.nav_drawer_bookmarks)
        .withSelectable(false)
        .withIcon(CommunityMaterial.Icon.cmd_bookmark_outline)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavBookmarks)
        .withSelectedTextColor(color(R.color.colorNavBookmarks))
        .withSelectedIconColorRes(R.color.colorNavBookmarks)

    drawerItemSettings = PrimaryDrawerItem()
        .withIdentifier(NavigationSection.SETTINGS)
        .withIcon(CommunityMaterial.Icon.cmd_settings)
        .withName(R.string.nav_drawer_settings)
        .withSelectable(false)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavSettings)
        .withSelectedTextColor(color(R.color.colorNavSettings))
        .withSelectedIconColorRes(R.color.colorNavSettings)

    drawerItemSignIn = PrimaryDrawerItem()
        .withIdentifier(NavigationSection.SIGN_IN)
        .withName(R.string.nav_drawer_sign_in)
        .withSelectable(false)
        .withIcon(CommunityMaterial.Icon.cmd_login)
        .withTextColor(color(R.color.colorNavDefaultText))
        .withIconColorRes(R.color.colorNavSignIn)
        .withSelectedTextColor(color(R.color.colorNavSignIn))
        .withSelectedIconColorRes(R.color.colorNavSignIn)

    drawerItemSignOut = PrimaryDrawerItem()
        .withIdentifier(NavigationSection.SIGN_OUT)
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

    val drawerBuilder = DrawerBuilder()
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
    // TODO() Implement App Links
  }
}
