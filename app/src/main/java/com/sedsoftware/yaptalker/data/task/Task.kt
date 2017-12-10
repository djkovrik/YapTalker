package com.sedsoftware.yaptalker.data.task

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver

/**
 * Abstract class for an Android related task which must be executed in background thread.
 * This interface represents a execution unit for different tasks (this means any background task in
 * data layer should implement this contract).
 *
 * By convention each Task implementation will return the result using a DisposableSingleObserver
 * that will execute its job in chosen thread and will post the result in the UI thread.
 */
abstract class Task<T, in Params>(private val executionScheduler: Scheduler) {

  abstract fun buildTaskSingle(params: Params): Single<T>

  private val disposables by lazy { CompositeDisposable() }

  fun execute(observer: DisposableSingleObserver<T>, params: Params) {

    requireNotNull(observer)

    buildTaskSingle(params)
        .subscribeOn(executionScheduler)
        .observeOn(AndroidSchedulers.mainThread())
        .also { single -> addToDisposables(single.subscribeWith(observer)) }
  }

  fun dispose() {
    if (!disposables.isDisposed) {
      disposables.dispose()
    }
  }

  private fun addToDisposables(disposable: Disposable) {

    requireNotNull(disposable)
    requireNotNull(disposables)

    disposables.add(disposable)
  }
}
