package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpView

interface BaseViewWithLoading : MvpView {

  fun updateAppbarTitle(title: String)

  fun showErrorMessage(message: String)

  fun showLoadingIndicator(shouldShow: Boolean)
}
