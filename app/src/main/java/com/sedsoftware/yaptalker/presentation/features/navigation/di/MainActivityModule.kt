package com.sedsoftware.yaptalker.presentation.features.navigation.di

import com.sedsoftware.yaptalker.data.repository.YapEulaTextRepository
import com.sedsoftware.yaptalker.data.repository.YapLoginSessionInfoRepository
import com.sedsoftware.yaptalker.data.service.YapSignOutService
import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.domain.repository.EulaTextRepository
import com.sedsoftware.yaptalker.domain.repository.LoginSessionInfoRepository
import com.sedsoftware.yaptalker.domain.service.SignOutService
import com.sedsoftware.yaptalker.presentation.features.navigation.MainActivity
import com.sedsoftware.yaptalker.presentation.features.navigation.MainActivityNavigator
import dagger.Binds
import dagger.Module
import dagger.Provides
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
  abstract fun loginSessionInfoRepository(repo: YapLoginSessionInfoRepository): LoginSessionInfoRepository

  @ActivityScope
  @Binds
  abstract fun eulaTextRepository(repo: YapEulaTextRepository): EulaTextRepository

  @ActivityScope
  @Binds
  abstract fun signOutService(service: YapSignOutService): SignOutService
}
