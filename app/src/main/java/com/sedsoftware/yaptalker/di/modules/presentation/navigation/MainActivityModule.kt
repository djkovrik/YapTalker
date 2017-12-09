package com.sedsoftware.yaptalker.di.modules.presentation.navigation

import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.presentation.features.navigation.MainActivityNavigator
import dagger.Binds
import dagger.Module
import ru.terrakok.cicerone.Navigator

@Module
abstract class MainActivityModule {

  @ActivityScope
  @Binds
  abstract fun mainActivityNavigator(navigator: MainActivityNavigator): Navigator
}
