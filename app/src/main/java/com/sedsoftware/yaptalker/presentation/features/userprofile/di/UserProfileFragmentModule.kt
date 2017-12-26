package com.sedsoftware.yaptalker.presentation.features.userprofile.di

import com.sedsoftware.yaptalker.data.repository.YapUserProfileRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.UserProfileRepository
import dagger.Binds
import dagger.Module

@Module
abstract class UserProfileFragmentModule {

  @FragmentScope
  @Binds
  abstract fun profileRepository(repo: YapUserProfileRepository): UserProfileRepository
}
