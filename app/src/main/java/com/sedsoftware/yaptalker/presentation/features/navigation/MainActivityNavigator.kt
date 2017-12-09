@file:Suppress("WhenWithOnlyElse")

package com.sedsoftware.yaptalker.presentation.features.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.sedsoftware.yaptalker.R
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import javax.inject.Inject

class MainActivityNavigator @Inject constructor(activity: MainActivity) :
    SupportAppNavigator(activity, activity.supportFragmentManager, R.id.content_frame) {

  override fun createActivityIntent(screenKey: String?, data: Any?): Intent? = when (screenKey) {
//    NavigationScreens.SETTINGS_SCREEN -> SettingsActivity.getIntent(this@MainActivity)
    else -> null
  }

  @Suppress("UNCHECKED_CAST")
  override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
//    NavigationScreens.NEWS_SCREEN -> NewsFragment.getNewInstance()
//    NavigationScreens.FORUMS_LIST_SCREEN -> ForumsFragment.getNewInstance()
//    NavigationScreens.CHOSEN_FORUM_SCREEN -> ChosenForumFragment.getNewInstance(data as Int)
//    NavigationScreens.ACTIVE_TOPICS_SCREEN -> ActiveTopicsFragment.getNewInstance()
//    NavigationScreens.BOOKMARKS_SCREEN -> BookmarksFragment.getNewInstance()
//    NavigationScreens.CHOSEN_TOPIC_SCREEN -> ChosenTopicFragment.getNewInstance(data as Triple<Int, Int, Int>)
//    NavigationScreens.USER_PROFILE_SCREEN -> UserProfileFragment.getNewInstance(data as Int)
//    NavigationScreens.AUTHORIZATION_SCREEN -> AuthorizationFragment.getNewInstance()
//    NavigationScreens.ADD_MESSAGE_SCREEN -> AddMessageFragment.getNewInstance(data as String)
    else -> null
  }

  override fun setupFragmentTransactionAnimation(command: Command?,
                                                 currentFragment: Fragment?,
                                                 nextFragment: Fragment?,
                                                 fragmentTransaction: FragmentTransaction?) {

    fragmentTransaction?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
  }
}
