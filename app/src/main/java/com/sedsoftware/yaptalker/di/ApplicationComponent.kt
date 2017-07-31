package com.sedsoftware.yaptalker.di

import com.sedsoftware.yaptalker.di.modules.ApplicationModule
import com.sedsoftware.yaptalker.di.modules.RequestsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, RequestsModule::class))
interface ApplicationComponent {
  // Injections here
}