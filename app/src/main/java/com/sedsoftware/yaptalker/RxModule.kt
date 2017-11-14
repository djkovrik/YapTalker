package com.sedsoftware.yaptalker

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.base.events.ConnectionState
import com.sedsoftware.yaptalker.base.navigation.NavigationDrawerItems

val rxModule = Kodein.Module {
  // Rx bus for appbar title changing events
  bind<BehaviorRelay<String>>() with singleton {
    BehaviorRelay.createDefault("")
  }

  // Rx bus for navDrawer selection events
  bind<BehaviorRelay<Long>>("navDrawer") with singleton {
    BehaviorRelay.createDefault(NavigationDrawerItems.MAIN_PAGE)
  }

  // Rx bus for connection state events
  bind<BehaviorRelay<Long>>("connectionState") with singleton {
    BehaviorRelay.createDefault(ConnectionState.IDLE)
  }
}
