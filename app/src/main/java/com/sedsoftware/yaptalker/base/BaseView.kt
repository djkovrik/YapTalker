package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

  fun showErrorMessage(message: String)

  fun showLoadingIndicator(shouldShow: Boolean) {
    // Default empty implementation
  }
}
