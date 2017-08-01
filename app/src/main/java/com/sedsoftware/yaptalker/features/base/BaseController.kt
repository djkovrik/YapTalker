package com.sedsoftware.yaptalker.features.base

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpDelegate
import com.bluelinelabs.conductor.Controller


abstract class BaseController : Controller {

  private val mvpDelegate: MvpDelegate<out BaseController> by lazy {
    MvpDelegate<BaseController>(this)
  }

  constructor() : super() {
    mvpDelegate.onCreate()
  }

  constructor(args: Bundle?) : super(args) {
    mvpDelegate.onCreate(args)
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
    mvpDelegate.onAttach()
  }

  override fun onDetach(view: View) {
    super.onDetach(view)
    mvpDelegate.onDetach()
  }

  override fun onDestroy() {
    super.onDestroy()
    mvpDelegate.onDestroy()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mvpDelegate.onSaveInstanceState(outState)
  }
}