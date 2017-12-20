package com.sedsoftware.yaptalker.di.modules.contribution

import com.sedsoftware.yaptalker.di.scopes.FragmentScope
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
}
