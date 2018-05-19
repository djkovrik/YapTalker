@file:Suppress("WhenWithOnlyElse")

package com.sedsoftware.yaptalker.presentation.feature.navigation

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.feature.activetopics.ActiveTopicsFragment
import com.sedsoftware.yaptalker.presentation.feature.authorization.AuthorizationFragment
import com.sedsoftware.yaptalker.presentation.feature.bookmarks.BookmarksFragment
import com.sedsoftware.yaptalker.presentation.feature.changelog.ChangelogActivity
import com.sedsoftware.yaptalker.presentation.feature.forum.ChosenForumFragment
import com.sedsoftware.yaptalker.presentation.feature.forumslist.ForumsFragment
import com.sedsoftware.yaptalker.presentation.feature.gallery.TopicGalleryActivity
import com.sedsoftware.yaptalker.presentation.feature.gifdisplay.GifDisplayActivity
import com.sedsoftware.yaptalker.presentation.feature.imagedisplay.ImageDisplayActivity
import com.sedsoftware.yaptalker.presentation.feature.incubator.IncubatorFragment
import com.sedsoftware.yaptalker.presentation.feature.news.NewsFragment
import com.sedsoftware.yaptalker.presentation.feature.posting.AddMessageFragment
import com.sedsoftware.yaptalker.presentation.feature.search.SearchFormFragment
import com.sedsoftware.yaptalker.presentation.feature.search.SearchRequest
import com.sedsoftware.yaptalker.presentation.feature.search.SearchResultsFragment
import com.sedsoftware.yaptalker.presentation.feature.settings.SettingsActivity
import com.sedsoftware.yaptalker.presentation.feature.topic.ChosenTopicFragment
import com.sedsoftware.yaptalker.presentation.feature.topic.GalleryInitialState
import com.sedsoftware.yaptalker.presentation.feature.updater.UpdaterFragment
import com.sedsoftware.yaptalker.presentation.feature.userprofile.UserProfileFragment
import com.sedsoftware.yaptalker.presentation.feature.videodisplay.VideoDisplayActivity
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MainActivityNavigator @Inject constructor(
  private val activity: MainActivity
) : SupportAppNavigator(activity, activity.supportFragmentManager, R.id.content_frame) {

  override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? = when (screenKey) {
    NavigationScreen.SETTINGS_SCREEN -> SettingsActivity.getIntent(activity)
    NavigationScreen.IMAGE_DISPLAY_SCREEN -> ImageDisplayActivity.getIntent(activity, data as String)
    NavigationScreen.VIDEO_DISPLAY_SCREEN -> VideoDisplayActivity.getIntent(activity, data as String)
    NavigationScreen.GIF_DISPLAY_SCREEN -> GifDisplayActivity.getIntent(activity, data as String)
    NavigationScreen.TOPIC_GALLERY -> TopicGalleryActivity.getIntent(activity, data as GalleryInitialState)
    NavigationScreen.CHANGELOG_SCREEN -> ChangelogActivity.getIntent(activity)
    else -> null
  }

  override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
    NavigationScreen.NEWS_SCREEN -> NewsFragment.getNewInstance()
    NavigationScreen.ACTIVE_TOPICS_SCREEN -> ActiveTopicsFragment.getNewInstance()
    NavigationScreen.INCUBATOR_SCREEN -> IncubatorFragment.getNewInstance()
    NavigationScreen.AUTHORIZATION_SCREEN -> AuthorizationFragment.getNewInstance()
    NavigationScreen.BOOKMARKS_SCREEN -> BookmarksFragment.getNewInstance()
    NavigationScreen.FORUMS_LIST_SCREEN -> ForumsFragment.getNewInstance()
    NavigationScreen.CHOSEN_FORUM_SCREEN -> ChosenForumFragment.getNewInstance(data as Pair<Int, String>)
    NavigationScreen.CHOSEN_TOPIC_SCREEN -> ChosenTopicFragment.getNewInstance(data as Triple<Int, Int, Int>)
    NavigationScreen.USER_PROFILE_SCREEN -> UserProfileFragment.getNewInstance(data as Int)
    NavigationScreen.MESSAGE_EDITOR_SCREEN -> AddMessageFragment.getNewInstance(data as Triple<String, String, String>)
    NavigationScreen.SEARCH_FORM -> SearchFormFragment.getNewInstance()
    NavigationScreen.SEARCH_RESULTS -> SearchResultsFragment.getNewInstance(data as SearchRequest)
    NavigationScreen.UPDATES_SCREEN -> UpdaterFragment.getNewInstance()
    else -> null
  }

  override fun setupFragmentTransactionAnimation(command: Command?,
                                                 currentFragment: Fragment?,
                                                 nextFragment: Fragment?,
                                                 fragmentTransaction: FragmentTransaction?) {

    fragmentTransaction?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
  }
}