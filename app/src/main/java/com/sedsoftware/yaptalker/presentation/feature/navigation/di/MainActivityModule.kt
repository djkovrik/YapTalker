package com.sedsoftware.yaptalker.presentation.feature.navigation.di

import com.sedsoftware.yaptalker.data.repository.YapLoginSessionRepository
import com.sedsoftware.yaptalker.di.scope.ActivityScope
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import com.sedsoftware.yaptalker.presentation.feature.activetopics.ActiveTopicsFragment
import com.sedsoftware.yaptalker.presentation.feature.activetopics.di.ActiveTopicsFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.authorization.AuthorizationFragment
import com.sedsoftware.yaptalker.presentation.feature.authorization.di.AuthorizationFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.bookmarks.BookmarksFragment
import com.sedsoftware.yaptalker.presentation.feature.bookmarks.di.BookmarksFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.forum.ChosenForumFragment
import com.sedsoftware.yaptalker.presentation.feature.forum.di.ChosenForumFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.forumslist.ForumsFragment
import com.sedsoftware.yaptalker.presentation.feature.forumslist.di.ForumsFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.incubator.IncubatorFragment
import com.sedsoftware.yaptalker.presentation.feature.incubator.di.IncubatorFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.navigation.MainActivity
import com.sedsoftware.yaptalker.presentation.feature.navigation.MainActivityNavigator
import com.sedsoftware.yaptalker.presentation.feature.news.NewsFragment
import com.sedsoftware.yaptalker.presentation.feature.news.di.NewsFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.posting.AddMessageFragment
import com.sedsoftware.yaptalker.presentation.feature.posting.di.AddMessageFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.search.SearchFormFragment
import com.sedsoftware.yaptalker.presentation.feature.search.SearchResultsFragment
import com.sedsoftware.yaptalker.presentation.feature.search.di.SearchFormFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.search.di.SearchResultsFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.topic.ChosenTopicFragment
import com.sedsoftware.yaptalker.presentation.feature.topic.di.ChosenTopicFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.updater.UpdaterFragment
import com.sedsoftware.yaptalker.presentation.feature.updater.di.UpdaterFragmentModule
import com.sedsoftware.yaptalker.presentation.feature.userprofile.UserProfileFragment
import com.sedsoftware.yaptalker.presentation.feature.userprofile.di.UserProfileFragmentModule
import com.sedsoftware.yaptalker.presentation.provider.ActionBarProvider
import com.sedsoftware.yaptalker.presentation.provider.NavDrawerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import ru.terrakok.cicerone.Navigator
import java.lang.ref.WeakReference

@Module
abstract class MainActivityModule {

    @Module
    companion object {
        @ActivityScope
        @Provides
        @JvmStatic
        fun provideMainActivityNavigator(activity: MainActivity): Navigator =
            MainActivityNavigator(activity)

        @ActivityScope
        @Provides
        @JvmStatic
        fun provideMessagesDelegate(activity: MainActivity): MessagesDelegate =
            MessagesDelegate(WeakReference(activity))
    }

    @ActivityScope
    @Binds
    abstract fun actionBarProvider(activity: MainActivity): ActionBarProvider

    @ActivityScope
    @Binds
    abstract fun navDrawerProvider(activity: MainActivity): NavDrawerProvider

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
    @ContributesAndroidInjector(modules = [(SearchFormFragmentModule::class)])
    abstract fun searchFormFragmentInjector(): SearchFormFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [(SearchResultsFragmentModule::class)])
    abstract fun searchResultsFragmentInjector(): SearchResultsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [(UpdaterFragmentModule::class)])
    abstract fun updaterFragmentInjector(): UpdaterFragment
}
