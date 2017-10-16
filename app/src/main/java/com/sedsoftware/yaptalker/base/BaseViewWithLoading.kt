package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpView

interface BaseViewWithLoading : MvpView {

  fun setAppbarTitle(title: String)

  fun showErrorMessage(message: String)

  fun showLoadingIndicator(isShown: Boolean)
}