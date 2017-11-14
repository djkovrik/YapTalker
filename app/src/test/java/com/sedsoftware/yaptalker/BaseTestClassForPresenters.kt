package com.sedsoftware.yaptalker

import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nhaarman.mockito_kotlin.mock
import com.sedsoftware.yaptalker.base.events.ConnectionState
import com.sedsoftware.yaptalker.base.navigation.NavigationDrawerItems
import com.sedsoftware.yaptalker.data.YapDataManager
import com.sedsoftware.yaptalker.data.requests.site.YapLoader
import com.sedsoftware.yaptalker.data.requests.site.YapSearchIdLoader
import com.sedsoftware.yaptalker.features.settings.SettingsHelper
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

abstract class BaseTestClassForPresenters {

  protected var settingsMock = mock<SettingsHelper>()
  protected var yapDataManagerMock = mock<YapDataManager>()

  val testKodein = Kodein {

    bind<Cicerone<Router>>() with singleton { Cicerone.create() }

    bind<Router>() with singleton { instance<Cicerone<Router>>().router }

    bind<BehaviorRelay<Long>>("connectionState") with singleton {
      BehaviorRelay.createDefault(ConnectionState.IDLE)
    }

    bind<BehaviorRelay<Long>>("navDrawer") with singleton {
      BehaviorRelay.createDefault(NavigationDrawerItems.MAIN_PAGE)
    }

    bind<BehaviorRelay<Long>>("") with singleton { BehaviorRelay.createDefault(ConnectionState.IDLE) }

    bind<BehaviorRelay<String>>() with singleton { BehaviorRelay.createDefault("") }

    bind<YapLoader>() with singleton { mock<YapLoader>() }

    bind<YapSearchIdLoader>() with singleton { mock<YapSearchIdLoader>() }

    bind<YapDataManager>() with singleton { yapDataManagerMock }

    bind<SettingsHelper>() with singleton { settingsMock }

    bind<ClearableCookieJar>() with singleton { mock<ClearableCookieJar>() }
  }
}
