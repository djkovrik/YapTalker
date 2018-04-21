package com.sedsoftware.yaptalker.presentation.feature.authorization.di

import com.sedsoftware.yaptalker.data.repository.YapLoginSessionRepository
import com.sedsoftware.yaptalker.data.repository.YapSitePreferencesRepository
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
import com.sedsoftware.yaptalker.domain.repository.SitePreferencesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class AuthorizationFragmentModule {

  @FragmentScope
  @Binds
  abstract fun loginSessionRepository(repo: YapLoginSessionRepository): LoginSessionRepository

  @FragmentScope
  @Binds
  abstract fun sitePreferencesRepository(repo: YapSitePreferencesRepository): SitePreferencesRepository
}
