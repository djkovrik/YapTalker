package com.sedsoftware.yaptalker.presentation.base.event

sealed class AppEvent {

  class AppbarEvent(val title: String) : AppEvent()

  class NavDrawerEvent(val itemId: Long) : AppEvent()
}
