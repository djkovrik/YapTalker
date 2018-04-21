package com.sedsoftware.yaptalker.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.common.exception.MissingAnnotationException
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Maybe

abstract class BaseFragment : MvpAppCompatFragment() {

  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()
  private lateinit var backPressHandler: BackPressHandler

  open fun onBackPressed(): Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onCreate(savedInstanceState)

    lifecycle.accept(FragmentLifecycle.CREATE)

    if (activity is BackPressHandler) {
      backPressHandler = activity as BackPressHandler
    } else {
      throw ClassCastException("Base activity must implement BackPressHandler interface.")
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

    val clazz = this::class.java
    if (clazz.isAnnotationPresent(LayoutResource::class.java)) {
      val layoutId = clazz.getAnnotation(LayoutResource::class.java).value
      return inflater.inflate(layoutId, container, false)
    } else {
      throw MissingAnnotationException("$this must be annotated with @LayoutResource annotation.")
    }
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    lifecycle.accept(FragmentLifecycle.ATTACH)
  }

  override fun onStart() {
    super.onStart()
    lifecycle.accept(FragmentLifecycle.START)
    backPressHandler.setSelectedFragment(this)
  }

  override fun onResume() {
    super.onResume()
    lifecycle.accept(FragmentLifecycle.RESUME)
  }

  override fun onPause() {
    super.onPause()
    lifecycle.accept(FragmentLifecycle.PAUSE)
  }

  override fun onStop() {
    super.onStop()
    lifecycle.accept(FragmentLifecycle.STOP)
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.accept(FragmentLifecycle.DESTROY)
  }

  override fun onDetach() {
    super.onDetach()
    lifecycle.accept(FragmentLifecycle.DETACH)
  }

  protected fun event(@FragmentLifecycle.Event event: Long): Maybe<*> =
    lifecycle.filter({ e -> e == event }).firstElement()
}
