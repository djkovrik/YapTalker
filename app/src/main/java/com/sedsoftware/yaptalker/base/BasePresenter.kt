package com.sedsoftware.yaptalker.base

import com.arellomobile.mvp.MvpPresenter

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>()
