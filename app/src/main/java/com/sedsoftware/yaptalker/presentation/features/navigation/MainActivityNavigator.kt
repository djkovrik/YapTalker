@file:Suppress("WhenWithOnlyElse")

package com.sedsoftware.yaptalker.presentation.features.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.features.activetopics.ActiveTopicsFragment
import com.sedsoftware.yaptalker.presentation.features.authorization.AuthorizationFragment
import com.sedsoftware.yaptalker.presentation.features.news.NewsFragment
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import javax.inject.Inject

class MainActivityNavigator @Inject constructor(activity: MainActivity) :
    SupportAppNavigator(activity, activity.supportFragmentManager, R.id.content_frame) {

  override fun createActivityIntent(screenKey: String?, data: Any?): Intent? = when (screenKey) {
//    NavigationScreen.SETTINGS_SCREEN -> SettingsActivity.getIntent(this@MainActivity)
    else -> null
  }

  @Suppress("UNCHECKED_CAST")
  override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
    NavigationScreen.NEWS_SCREEN -> NewsFragment.getNewInstance()
    NavigationScreen.ACTIVE_TOPICS_SCREEN -> ActiveTopicsFragment.getNewInstance()
    NavigationScreen.AUTHORIZATION_SCREEN -> AuthorizationFragment.getNewInstance()
//    NavigationScreen.FORUMS_LIST_SCREEN -> ForumsFragment.getNewInstance()
//    NavigationScreen.CHOSEN_FORUM_SCREEN -> ChosenForumFragment.getNewInstance(data as Int)
//    NavigationScreen.BOOKMARKS_SCREEN -> BookmarksFragment.getNewInstance()
//    NavigationScreen.CHOSEN_TOPIC_SCREEN -> ChosenTopicFragment.getNewInstance(data as Triple<Int, Int, Int>)
//    NavigationScreen.USER_PROFILE_SCREEN -> UserProfileFragment.getNewInstance(data as Int)
//    NavigationScreen.ADD_MESSAGE_SCREEN -> AddMessageFragment.getNewInstance(data as String)
    else -> null
  }

  override fun setupFragmentTransactionAnimation(command: Command?,
                                                 currentFragment: Fragment?,
                                                 nextFragment: Fragment?,
                                                 fragmentTransaction: FragmentTransaction?) {

    fragmentTransaction?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
  }
}
