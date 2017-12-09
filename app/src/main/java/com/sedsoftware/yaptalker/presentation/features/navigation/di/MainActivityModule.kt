package com.sedsoftware.yaptalker.presentation.features.navigation.di

import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.presentation.features.navigation.MainActivity
import com.sedsoftware.yaptalker.presentation.features.navigation.MainActivityNavigator
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Navigator

@Module
class MainActivityModule {

  @ActivityScope
  @Provides
  fun provideMainActivityNavigator(activity: MainActivity): Navigator = MainActivityNavigator(activity)
}
