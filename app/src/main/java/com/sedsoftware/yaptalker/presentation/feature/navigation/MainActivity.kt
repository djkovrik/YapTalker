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
import com.mikepenz.typeicons_typeface_library.Typeicons
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
import kotlinx.android.synthetic.main.activity_main_tablets.navigation_drawer
import kotlinx.android.synthetic.main.include_main_appbar.toolbar
import ru.terrakok.cicerone.Navigator
import timber.log.Timber
import javax.inject.Inject

@LayoutResourceTablets(normalValue = R.layout.activity_main, tabletsValue = R.layout.activity_main_tablets)
class MainActivity : BaseActivity(), MainActivityView, ActionBarProvider, NavDrawerProvider {

    companion object {
        private const val ACTION_NAVIGATE_TO_MAIN =
            "com.sedsoftware.yaptalker.ACTION_NAVIGATE_TO_MAIN"
        private const val ACTION_NAVIGATE_TO_FORUMS =
            "com.sedsoftware.yaptalker.ACTION_NAVIGATE_TO_FORUMS"
        private const val ACTION_NAVIGATE_TO_ACTIVE_TOPICS =
            "com.sedsoftware.yaptalker.ACTION_NAVIGATE_TO_ACTIVE_TOPICS"
        private const val ACTION_NAVIGATE_TO_INCUBATOR =
            "com.sedsoftware.yaptalker.ACTION_NAVIGATE_TO_INCUBATOR"

        private const val BOOKMARKS_INSERT_POSITION = 5
        private const val SIGN_IN_INSERT_POSITION = 16
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
    private lateinit var drawerItemBookmarks: PrimaryDrawerItem
    private lateinit var drawerItemPictures: PrimaryDrawerItem
    private lateinit var drawerItemVideos: PrimaryDrawerItem
    private lateinit var drawerItemEvents: PrimaryDrawerItem
    private lateinit var drawerItemAutoMoto: PrimaryDrawerItem
    private lateinit var drawerItemAnimals: PrimaryDrawerItem
    private lateinit var drawerItemPhotobomb: PrimaryDrawerItem
    private lateinit var drawerItemIncubator: PrimaryDrawerItem
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
        navDrawer.addItemAtPosition(drawerItemBookmarks, BOOKMARKS_INSERT_POSITION)
        navDrawer.addItemAtPosition(drawerItemSignOut, SIGN_IN_INSERT_POSITION)
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
            .withIcon(Typeicons.Icon.typ_home)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavMainPage))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavMainPage))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavMainPage))

        drawerItemForums = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.FORUMS)
            .withName(R.string.nav_drawer_forums)
            .withIcon(Typeicons.Icon.typ_group)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavForums))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavForums))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavForums))

        drawerItemActiveTopics = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.ACTIVE_TOPICS)
            .withName(R.string.nav_drawer_active_topics)
            .withIcon(Typeicons.Icon.typ_star)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavActiveTopics))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavActiveTopics))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavActiveTopics))

        drawerItemBookmarks = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.BOOKMARKS)
            .withName(R.string.nav_drawer_bookmarks)
            .withIcon(Typeicons.Icon.typ_bookmark)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavBookmarks))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavBookmarks))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavBookmarks))

        drawerItemPictures = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.PICTURES)
            .withName(R.string.nav_drawer_pictures)
            .withIcon(Typeicons.Icon.typ_image)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavPictures))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavPictures))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavPictures))

        drawerItemPictures = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.PICTURES)
            .withName(R.string.nav_drawer_pictures)
            .withIcon(Typeicons.Icon.typ_image)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavPictures))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavPictures))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavPictures))

        drawerItemVideos = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.VIDEO)
            .withName(R.string.nav_drawer_video)
            .withIcon(Typeicons.Icon.typ_video)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavVideos))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavVideos))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavVideos))

        drawerItemEvents = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.EVENTS)
            .withName(R.string.nav_drawer_events)
            .withIcon(Typeicons.Icon.typ_news)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavEvents))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavEvents))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavEvents))

        drawerItemAutoMoto = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.AUTO_MOTO)
            .withName(R.string.nav_drawer_auto_moto)
            .withIcon(CommunityMaterial.Icon.cmd_car)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavAutoMoto))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavAutoMoto))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavAutoMoto))

        drawerItemAnimals = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.ANIMALS)
            .withName(R.string.nav_drawer_animals)
            .withIcon(CommunityMaterial.Icon.cmd_cat)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavAnimals))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavAnimals))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavAnimals))

        drawerItemPhotobomb = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.PHOTOBOMB)
            .withName(R.string.nav_drawer_photobomb)
            .withIcon(Typeicons.Icon.typ_camera_outline)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavPhotobomb))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavPhotobomb))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavPhotobomb))

        drawerItemIncubator = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.INCUBATOR)
            .withName(R.string.nav_drawer_incubator)
            .withIcon(Typeicons.Icon.typ_user)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavIncubator))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavIncubator))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavIncubator))

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
            .withIcon(Typeicons.Icon.typ_cog)
            .withName(R.string.nav_drawer_settings)
            .withTextColor(colorFromAttr(R.attr.colorNavDefaultText))
            .withIconColor(colorFromAttr(R.attr.colorNavSettings))
            .withSelectedTextColor(colorFromAttr(R.attr.colorNavSettings))
            .withSelectedIconColor(colorFromAttr(R.attr.colorNavSettings))

        drawerItemUpdater = PrimaryDrawerItem()
            .withIdentifier(NavigationSection.APP_UPDATES)
            .withIcon(Typeicons.Icon.typ_download)
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
            .addDrawerItems(DividerDrawerItem())
            .addDrawerItems(drawerItemPictures)
            .addDrawerItems(drawerItemVideos)
            .addDrawerItems(drawerItemEvents)
            .addDrawerItems(drawerItemAutoMoto)
            .addDrawerItems(drawerItemAnimals)
            .addDrawerItems(drawerItemPhotobomb)
            .addDrawerItems(drawerItemIncubator)
            .addDrawerItems(DividerDrawerItem())
            .addDrawerItems(drawerItemSearch)
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
        val appLinkData = appLinkIntent.data

        when (intent.action) {
            ACTION_NAVIGATE_TO_MAIN -> presenter.navigateToMain()
            ACTION_NAVIGATE_TO_FORUMS -> presenter.navigateToForums()
            ACTION_NAVIGATE_TO_ACTIVE_TOPICS -> presenter.navigateToActiveTopics()
            ACTION_NAVIGATE_TO_INCUBATOR -> presenter.navigateToIncubator()
            Intent.ACTION_VIEW -> {
                if (appLinkData != null) {
                    val navigateTo = appLinkData.toString().extractYapIds()
                    if (navigateTo.second != 0) {
                        presenter.navigateWithIntentLink(navigateTo)
                    }
                }
            }
            else -> presenter.navigateToDefaultHomePage()
        }
    }
}
