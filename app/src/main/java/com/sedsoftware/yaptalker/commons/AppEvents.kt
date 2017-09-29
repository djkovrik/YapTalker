package com.sedsoftware.yaptalker.commons

open class AppEvent {

  companion object {
    const val BASE = 0L
    const val REQUEST_STATE = 1L
    const val UPDATE_APPBAR = 2L
    const val UPDATE_NAVDRAWER = 3L
  }

  open fun getType() = AppEvent.BASE
}

class RequestStateEvent(val connected: Boolean) : AppEvent() {
  override fun getType() = AppEvent.REQUEST_STATE
}

class UpdateAppbarEvent(val title: String) : AppEvent() {
  override fun getType() = AppEvent.UPDATE_APPBAR
}

class UpdateNavDrawerEvent(val name: String, val title: String, val avatar: String) : AppEvent() {
  override fun getType() = AppEvent.UPDATE_NAVDRAWER
}
