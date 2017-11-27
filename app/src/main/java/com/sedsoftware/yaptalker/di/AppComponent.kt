package com.sedsoftware.yaptalker.di

import android.content.Context
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.di.modules.ActivityContributionModule
import com.sedsoftware.yaptalker.di.modules.AppModule
import com.sedsoftware.yaptalker.di.modules.FragmentContributionModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    AppModule::class,
    ActivityContributionModule::class,
    FragmentContributionModule::class
))
interface AppComponent {

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun context(context: Context): Builder

    fun build(): AppComponent
  }

  fun inject(app: YapTalkerApp)
}
