package com.sedsoftware.yaptalker.di.modules.network.cookies

import com.sedsoftware.yaptalker.domain.device.Settings
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CookiesModule {

  @Singleton
  @Provides
  fun provideCookieStorage(settings: Settings): CustomCookieStorage = CustomCookieStorage(settings)
}
