package com.sedsoftware.yaptalker.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.presentation.base.lifecycle.FragmentLifecycle
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Maybe

abstract class BaseFragment : MvpAppCompatFragment() {

  protected abstract val layoutId: Int

  private val lifecycle: BehaviorRelay<Long> = BehaviorRelay.create()

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onCreate(savedInstanceState)
    lifecycle.accept(FragmentLifecycle.CREATE)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
      inflater.inflate(layoutId, container, false)

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    lifecycle.accept(FragmentLifecycle.ATTACH)
  }

  override fun onStart() {
    super.onStart()
    lifecycle.accept(FragmentLifecycle.START)
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
