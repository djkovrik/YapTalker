package com.sedsoftware.yaptalker.presentation.features.navigation.di

import com.sedsoftware.yaptalker.data.repository.YapLoginSessionRepository
import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import com.sedsoftware.yaptalker.presentation.features.activetopics.ActiveTopicsFragment
import com.sedsoftware.yaptalker.presentation.features.activetopics.di.ActiveTopicsFragmentModule
import com.sedsoftware.yaptalker.presentation.features.authorization.AuthorizationFragment
import com.sedsoftware.yaptalker.presentation.features.authorization.di.AuthorizationFragmentModule
import com.sedsoftware.yaptalker.presentation.features.bookmarks.BookmarksFragment
import com.sedsoftware.yaptalker.presentation.features.bookmarks.di.BookmarksFragmentModule
import com.sedsoftware.yaptalker.presentation.features.forum.ChosenForumFragment
import com.sedsoftware.yaptalker.presentation.features.forum.di.ChosenForumFragmentModule
import com.sedsoftware.yaptalker.presentation.features.forumslist.ForumsFragment
import com.sedsoftware.yaptalker.presentation.features.forumslist.di.ForumsFragmentModule
import com.sedsoftware.yaptalker.presentation.features.incubator.IncubatorFragment
import com.sedsoftware.yaptalker.presentation.features.incubator.di.IncubatorFragmentModule
import com.sedsoftware.yaptalker.presentation.features.navigation.MainActivity
import com.sedsoftware.yaptalker.presentation.features.navigation.MainActivityNavigator
import com.sedsoftware.yaptalker.presentation.features.news.NewsFragment
import com.sedsoftware.yaptalker.presentation.features.news.di.NewsFragmentModule
import com.sedsoftware.yaptalker.presentation.features.posting.AddMessageFragment
import com.sedsoftware.yaptalker.presentation.features.posting.di.AddMessageFragmentModule
import com.sedsoftware.yaptalker.presentation.features.topic.ChosenTopicFragment
import com.sedsoftware.yaptalker.presentation.features.topic.di.ChosenTopicFragmentModule
import com.sedsoftware.yaptalker.presentation.features.userprofile.UserProfileFragment
import com.sedsoftware.yaptalker.presentation.features.userprofile.di.UserProfileFragmentModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import ru.terrakok.cicerone.Navigator

@Module
abstract class MainActivityModule {

  @Module
  companion object {
    @ActivityScope
    @Provides
    @JvmStatic
    fun provideMainActivityNavigator(activity: MainActivity): Navigator = MainActivityNavigator(activity)
  }

  @ActivityScope
  @Binds
  abstract fun loginSessionRepository(repo: YapLoginSessionRepository): LoginSessionRepository

  @FragmentScope
  @ContributesAndroidInjector(modules = [(NewsFragmentModule::class)])
  abstract fun newsFragmentInjector(): NewsFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(ActiveTopicsFragmentModule::class)])
  abstract fun activeTopicsFragmentInjector(): ActiveTopicsFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(AuthorizationFragmentModule::class)])
  abstract fun authorizationFragmentInjector(): AuthorizationFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(BookmarksFragmentModule::class)])
  abstract fun bookmarksFragmentInjector(): BookmarksFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(ForumsFragmentModule::class)])
  abstract fun forumsListFragmentInjector(): ForumsFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(ChosenForumFragmentModule::class)])
  abstract fun chosenForumFragmentInjector(): ChosenForumFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(ChosenTopicFragmentModule::class)])
  abstract fun chosenTopicFragmentInjector(): ChosenTopicFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(UserProfileFragmentModule::class)])
  abstract fun userProfileFragmentInjector(): UserProfileFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(AddMessageFragmentModule::class)])
  abstract fun addMessageFragmentInjector(): AddMessageFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(IncubatorFragmentModule::class)])
  abstract fun incubatorFragmentInjector(): IncubatorFragment
}
