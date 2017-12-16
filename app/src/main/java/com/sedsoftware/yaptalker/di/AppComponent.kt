package com.sedsoftware.yaptalker.di

import android.content.Context
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.di.modules.AppModule
import com.sedsoftware.yaptalker.di.modules.NavigationModule
import com.sedsoftware.yaptalker.di.modules.NetworkModule
import com.sedsoftware.yaptalker.di.modules.RxModule
import com.sedsoftware.yaptalker.di.modules.contribution.ActivityContributionModule
import com.sedsoftware.yaptalker.di.modules.contribution.FragmentContributionModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
  (ActivityContributionModule::class),
  (FragmentContributionModule::class),
  (AppModule::class),
  (NavigationModule::class),
  (NetworkModule::class),
  (RxModule::class)
])
interface AppComponent {

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun context(context: Context): Builder

    fun build(): AppComponent
  }

  fun inject(app: YapTalkerApp)
}
