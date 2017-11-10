package com.sedsoftware.yaptalker

fun callProtectedPresenterMethod(presenter: Any, methodName: String) {
  presenter.javaClass.getDeclaredMethod(methodName).apply {
    isAccessible = true
    invoke(presenter)
  }
}
