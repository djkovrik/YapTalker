package com.sedsoftware.yaptalker.presentation.feature.navigation

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.device.settings.DefaultHomeScreen
import com.sedsoftware.yaptalker.device.storage.state.TopicStateStorage
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.AuthorizationInteractor
import com.sedsoftware.yaptalker.domain.interactor.LoginSessionInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.sedsoftware.yaptalker.presentation.mapper.LoginSessionInfoModelMapper
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel
import com.uber.autodispose.kotlin.autoDisposable
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MainActivityPresenter @Inject constructor(
    private val router: Router,
    private val settings: Settings,
    private val loginSessionInteractor: LoginSessionInteractor,
    private val sessionInfoMapper: LoginSessionInfoModelMapper,
    private val topicStateStorage: TopicStateStorage,
    private val authorizationInteractor: AuthorizationInteractor,
    private val schedulers: SchedulersProvider
) : BasePresenter<MainActivityView>() {

    init {
        router.setResultListener(RequestCode.SIGN_IN) {
            refreshAuthorization()
            navigateToDefaultHomePage()
        }
    }

    private var currentUserKey = ""
    private var currentUserId = 0
    private var isLinkNavigationPending = false
    private var isStartupLaunchNavigated = false
    private var triedToRestoreSession = false

    override fun attachView(view: MainActivityView?) {
        super.attachView(view)
        refreshAuthorization()
    }

    override fun onDestroy() {
        super.onDestroy()
        router.removeResultListener(RequestCode.SIGN_IN)
    }

    @Suppress("ComplexMethod")
    fun navigateToChosenSection(@NavigationSection.Section identifier: Long) {
        when (identifier) {
            NavigationSection.MAIN_PAGE -> router.newRootScreen(NavigationScreen.NEWS_SCREEN)
            NavigationSection.FORUMS -> router.newRootScreen(NavigationScreen.FORUMS_LIST_SCREEN)
            NavigationSection.ACTIVE_TOPICS -> router.newRootScreen(NavigationScreen.ACTIVE_TOPICS_SCREEN)
            NavigationSection.BOOKMARKS -> router.navigateTo(NavigationScreen.BOOKMARKS_SCREEN)
            NavigationSection.PICTURES -> router.navigateTo(NavigationScreen.PICTURES_SCREEN)
            NavigationSection.VIDEO -> router.navigateTo(NavigationScreen.VIDEOS_SCREEN)
            NavigationSection.EVENTS -> router.navigateTo(NavigationScreen.EVENTS_SCREEN)
            NavigationSection.AUTO_MOTO -> router.navigateTo(NavigationScreen.AUTO_MOTO_SCREEN)
            NavigationSection.ANIMALS -> router.navigateTo(NavigationScreen.ANIMALS_SCREEN)
            NavigationSection.PHOTOBOMB -> router.navigateTo(NavigationScreen.PHOTOBOMB_SCREEN)
            NavigationSection.INCUBATOR -> router.navigateTo(NavigationScreen.INCUBATOR_SCREEN)
            NavigationSection.SITE_SEARCH -> router.newRootScreen(NavigationScreen.SEARCH_FORM)
            NavigationSection.SETTINGS -> router.navigateTo(NavigationScreen.SETTINGS_SCREEN)
            NavigationSection.APP_UPDATES -> router.newRootScreen(NavigationScreen.UPDATES_SCREEN)
            NavigationSection.SIGN_IN -> router.navigateTo(NavigationScreen.AUTHORIZATION_SCREEN)
            NavigationSection.SIGN_OUT -> sendSignOutRequest()
        }
    }

    fun navigateWithIntentLink(triple: Triple<Int, Int, Int>) {
        isLinkNavigationPending = true
        router.navigateTo(NavigationScreen.CHOSEN_TOPIC_SCREEN, triple)
    }

    fun navigateToUserProfile() {
        if (currentUserId != 0) {
            viewState.closeNavigationDrawer()
            router.navigateTo(NavigationScreen.USER_PROFILE_SCREEN, currentUserId)
        }
    }

    fun navigateToMain() {
        isStartupLaunchNavigated = true
        router.newRootScreen(NavigationScreen.NEWS_SCREEN)
    }

    fun navigateToForums() {
        isStartupLaunchNavigated = true
        router.newRootScreen(NavigationScreen.FORUMS_LIST_SCREEN)
    }

    fun navigateToActiveTopics() {
        isStartupLaunchNavigated = true
        router.newRootScreen(NavigationScreen.ACTIVE_TOPICS_SCREEN)
    }

    fun navigateToIncubator() {
        isStartupLaunchNavigated = true
        router.newRootScreen(NavigationScreen.INCUBATOR_SCREEN)
    }

    fun navigateToDefaultHomePage() {
        if (isLinkNavigationPending) {
            isLinkNavigationPending = false
            return
        }

        if (isStartupLaunchNavigated) {
            return
        }

        isStartupLaunchNavigated = true

        val savedTopicState = topicStateStorage.getState()

        when {
            savedTopicState != null && savedTopicState.forumId != 0 && savedTopicState.topicId != 0 -> {
                router.newRootScreen(NavigationScreen.RESTORED_TOPIC_SCREEN, savedTopicState)
            }
            settings.getStartingPage() == DefaultHomeScreen.FORUMS -> {
                router.newRootScreen(NavigationScreen.FORUMS_LIST_SCREEN)
            }
            settings.getStartingPage() == DefaultHomeScreen.ACTIVE_TOPICS -> {
                router.newRootScreen(NavigationScreen.ACTIVE_TOPICS_SCREEN)
            }
            settings.getStartingPage() == DefaultHomeScreen.INCUBATOR -> {
                router.newRootScreen(NavigationScreen.INCUBATOR_SCREEN)
            }
            else -> {
                router.newRootScreen(NavigationScreen.NEWS_SCREEN)
            }
        }
    }

    private fun refreshAuthorization() {
        loginSessionInteractor
            .getLoginSessionInfo()
            .map(sessionInfoMapper)
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({ info ->
                displayLoginSessionInfo(info)
                Timber.i("Login session info updated.")
            }, { e: Throwable ->
                Timber.e("Login session refresh error: ${e.message}")
            })
    }

    private fun displayLoginSessionInfo(sessionInfo: LoginSessionInfoModel) {
        currentUserKey = sessionInfo.sessionId
        currentUserId = sessionInfo.userId

        viewState.updateNavDrawerProfile(sessionInfo)
        viewState.clearDynamicNavigationItems()

        if (sessionInfo.nickname.isEmpty()) {
            if (!triedToRestoreSession) {
                triedToRestoreSession = true
                tryToRestoreSession()
            } else {
                viewState.displaySignedOutNavigation()
            }
        } else {
            viewState.displaySignedInNavigation()
        }
    }

    private fun tryToRestoreSession() {
        authorizationInteractor
            .tryToRestoreSession()
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                refreshAuthorization()
                Timber.i("Session restored.")
            }, { e: Throwable ->
                Timber.e("Session restore failed: ${e.message}")
                viewState.displaySignedOutNavigation()
            })
    }

    private fun sendSignOutRequest() {
        loginSessionInteractor
            .sendSignOutRequest(currentUserKey)
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({
                refreshAuthorization()
                navigateToDefaultHomePage()
                Timber.i("Sign Out request completed.")
            }, { e: Throwable ->
                Timber.e("Sign Out error: ${e.message}")
            })
    }
}
