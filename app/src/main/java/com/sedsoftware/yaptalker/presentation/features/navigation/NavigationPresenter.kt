package com.sedsoftware.yaptalker.presentation.features.navigation

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.navigation.NavigationDrawerItems
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class NavigationPresenter @Inject constructor(private val router: Router) : BasePresenter<NavigationView>() {

  fun navigateWithIntentLink(triple: Triple<Int, Int, Int>) {
//    router.navigateTo(NavigationScreens.CHOSEN_TOPIC_SCREEN, triple)
  }

  fun navigateToChosenSection(@NavigationDrawerItems.Section identifier: Long) {
    when (identifier) {
//      NavigationDrawerItems.MAIN_PAGE -> router.newRootScreen(NavigationScreens.NEWS_SCREEN)
//      NavigationDrawerItems.FORUMS -> router.newRootScreen(NavigationScreens.FORUMS_LIST_SCREEN)
//      NavigationDrawerItems.ACTIVE_TOPICS -> router.newRootScreen(NavigationScreens.ACTIVE_TOPICS_SCREEN)
//      NavigationDrawerItems.BOOKMARKS -> router.newRootScreen(NavigationScreens.BOOKMARKS_SCREEN)
//      NavigationDrawerItems.SETTINGS -> router.navigateTo(NavigationScreens.SETTINGS_SCREEN)
//      NavigationDrawerItems.SIGN_IN -> router.navigateTo(NavigationScreens.AUTHORIZATION_SCREEN)
//      NavigationDrawerItems.SIGN_OUT -> sendSignOutRequest()
    }
  }
}
