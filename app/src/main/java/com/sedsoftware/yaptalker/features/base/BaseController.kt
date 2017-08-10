package com.sedsoftware.yaptalker.features.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.arellomobile.mvp.MvpDelegate
import com.bluelinelabs.conductor.Controller


abstract class BaseController : Controller {

  private val mvpDelegate: MvpDelegate<out BaseController> by lazy {
    MvpDelegate<BaseController>(this)
  }

  lateinit var unbinder: Unbinder

  constructor() : super() {
    mvpDelegate.onCreate()
  }

  constructor(args: Bundle?) : super(args) {
    mvpDelegate.onCreate(args)
  }

  protected abstract fun getLayoutId(): Int
  protected abstract fun onViewBound(view: View)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val view = inflater.inflate(getLayoutId(), container, false)
    onViewBound(view)
    unbinder = ButterKnife.bind(this, view)
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
    unbinder.unbind()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mvpDelegate.onSaveInstanceState(outState)
  }
}