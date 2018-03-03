package com.sedsoftware.yaptalker.presentation.base.events

sealed class AppEvent {

  class AppbarEvent(val title: String) : AppEvent()

  class ConnectionEvent(val state: Int) : AppEvent()

  class NavDrawerEvent(val itemId: Int) : AppEvent()
}
