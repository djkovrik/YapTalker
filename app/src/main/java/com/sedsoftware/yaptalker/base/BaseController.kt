package com.sedsoftware.yaptalker.base

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpDelegate
import com.bluelinelabs.conductor.RestoreViewOnCreateController

abstract class BaseController : RestoreViewOnCreateController {

  private val mvpDelegate: MvpDelegate<out BaseController> by lazy {
    MvpDelegate<BaseController>(this)
  }

  constructor() : super() {
    mvpDelegate.onCreate()
  }

  constructor(args: Bundle?) : super(args) {
    mvpDelegate.onCreate(args)
  }

  abstract val controllerToolbar: Toolbar?

  protected abstract val controllerLayoutId: Int

  protected abstract fun onViewBound(view: View, savedViewState: Bundle?)

  protected abstract fun subscribeViews(parent: View)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                            savedViewState: Bundle?): View {

    val view = inflater.inflate(controllerLayoutId, container, false)
    onViewBound(view, savedViewState)
    subscribeViews(view)
    return view
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
