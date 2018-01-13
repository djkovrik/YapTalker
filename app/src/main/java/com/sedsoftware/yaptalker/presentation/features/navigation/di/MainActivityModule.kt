package com.sedsoftware.yaptalker.presentation.features.navigation.di

import com.sedsoftware.yaptalker.data.repository.YapLoginSessionRepository
import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.domain.repository.LoginSessionRepository
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
  abstract fun loginSessionRepository(repo: YapLoginSessionRepository): LoginSessionRepository
}
