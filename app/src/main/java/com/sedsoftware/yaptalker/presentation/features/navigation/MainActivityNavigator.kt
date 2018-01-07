@file:Suppress("WhenWithOnlyElse")

package com.sedsoftware.yaptalker.presentation.features.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.features.activetopics.ActiveTopicsFragment
import com.sedsoftware.yaptalker.presentation.features.authorization.AuthorizationFragment
import com.sedsoftware.yaptalker.presentation.features.bookmarks.BookmarksFragment
import com.sedsoftware.yaptalker.presentation.features.forum.ChosenForumFragment
import com.sedsoftware.yaptalker.presentation.features.forumslist.ForumsFragment
import com.sedsoftware.yaptalker.presentation.features.gifdisplay.GifDisplayActivity
import com.sedsoftware.yaptalker.presentation.features.imagedisplay.ImageDisplayActivity
import com.sedsoftware.yaptalker.presentation.features.incubator.IncubatorFragment
import com.sedsoftware.yaptalker.presentation.features.news.NewsFragment
import com.sedsoftware.yaptalker.presentation.features.posting.AddMessageFragment
import com.sedsoftware.yaptalker.presentation.features.settings.SettingsActivity
import com.sedsoftware.yaptalker.presentation.features.topic.ChosenTopicFragment
import com.sedsoftware.yaptalker.presentation.features.userprofile.UserProfileFragment
import com.sedsoftware.yaptalker.presentation.features.videodisplay.VideoDisplayActivity
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import javax.inject.Inject

class MainActivityNavigator @Inject constructor(
    private val activity: MainActivity
) : SupportAppNavigator(activity, activity.supportFragmentManager, R.id.content_frame) {

  override fun createActivityIntent(screenKey: String?, data: Any?): Intent? = when (screenKey) {
    NavigationScreen.SETTINGS_SCREEN -> SettingsActivity.getIntent(activity)
    NavigationScreen.IMAGE_DISPLAY_SCREEN -> ImageDisplayActivity.getIntent(activity, data as String)
    NavigationScreen.VIDEO_DISPLAY_SCREEN -> VideoDisplayActivity.getIntent(activity, data as String)
    NavigationScreen.GIF_DISPLAY_SCREEN -> GifDisplayActivity.getIntent(activity, data as String)
    else -> null
  }

  @Suppress("UNCHECKED_CAST")
  override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
    NavigationScreen.NEWS_SCREEN -> NewsFragment.getNewInstance()
    NavigationScreen.ACTIVE_TOPICS_SCREEN -> ActiveTopicsFragment.getNewInstance()
    NavigationScreen.INCUBATOR_SCREEN -> IncubatorFragment.getNewInstance()
    NavigationScreen.AUTHORIZATION_SCREEN -> AuthorizationFragment.getNewInstance()
    NavigationScreen.BOOKMARKS_SCREEN -> BookmarksFragment.getNewInstance()
    NavigationScreen.FORUMS_LIST_SCREEN -> ForumsFragment.getNewInstance()
    NavigationScreen.CHOSEN_FORUM_SCREEN -> ChosenForumFragment.getNewInstance(data as Int)
    NavigationScreen.CHOSEN_TOPIC_SCREEN -> ChosenTopicFragment.getNewInstance(data as Triple<Int, Int, Int>)
    NavigationScreen.USER_PROFILE_SCREEN -> UserProfileFragment.getNewInstance(data as Int)
    NavigationScreen.ADD_MESSAGE_SCREEN -> AddMessageFragment.getNewInstance(data as String)
    else -> null
  }

  override fun setupFragmentTransactionAnimation(command: Command?,
                                                 currentFragment: Fragment?,
                                                 nextFragment: Fragment?,
                                                 fragmentTransaction: FragmentTransaction?) {

    fragmentTransaction?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
  }
}
