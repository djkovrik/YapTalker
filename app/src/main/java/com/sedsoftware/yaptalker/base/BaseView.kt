package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

  fun setAppbarTitle(title: String)

  fun showErrorMessage(message: String)
}
