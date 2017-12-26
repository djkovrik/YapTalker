package com.sedsoftware.yaptalker.presentation.features.authorization.di

import com.sedsoftware.yaptalker.data.service.YapSignInService
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.service.SignInService
import dagger.Binds
import dagger.Module

@Module
abstract class AuthorizationFragmentModule {

  @FragmentScope
  @Binds
  abstract fun signInService(service: YapSignInService): SignInService
}
