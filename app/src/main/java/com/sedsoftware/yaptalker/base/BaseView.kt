package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

  fun showErrorMessage(message: String)
}
