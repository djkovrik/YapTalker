package com.sedsoftware.yaptalker.di.modules.contribution

import com.sedsoftware.yaptalker.di.scopes.FragmentScope
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
import com.sedsoftware.yaptalker.presentation.features.news.NewsFragment
import com.sedsoftware.yaptalker.presentation.features.news.di.NewsFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [(AndroidSupportInjectionModule::class)])
interface FragmentContributionModule {

  @FragmentScope
  @ContributesAndroidInjector(modules = [(NewsFragmentModule::class)])
  fun newsFragmentInjector(): NewsFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(ActiveTopicsFragmentModule::class)])
  fun activeTopicsFragmentInjector(): ActiveTopicsFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(AuthorizationFragmentModule::class)])
  fun authorizationFragmentInjector(): AuthorizationFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(BookmarksFragmentModule::class)])
  fun bookmarksFragmentInjector(): BookmarksFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(ForumsFragmentModule::class)])
  fun forumsListFragmentInjector(): ForumsFragment

  @FragmentScope
  @ContributesAndroidInjector(modules = [(ChosenForumFragmentModule::class)])
  fun chosenForumFragmentInjector(): ChosenForumFragment
}
