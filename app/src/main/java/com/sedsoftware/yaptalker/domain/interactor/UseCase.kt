package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.executor.ExecutionScheduler
import com.sedsoftware.yaptalker.domain.executor.PostExecutionScheduler
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

abstract class UseCase<T, in Params>(
    private val executionScheduler: ExecutionScheduler,
    private val postExecutionScheduler: PostExecutionScheduler) {

  abstract fun buildUseCaseObservable(params: Params): Observable<T>

  private val disposables by lazy { CompositeDisposable() }

  fun execute(observer: DisposableObserver<T>, params: Params) {
    requireNotNull(observer)

    buildUseCaseObservable(params)
        .subscribeOn(executionScheduler.getScheduler())
        .observeOn(postExecutionScheduler.getScheduler())
        .also { observable -> addToDisposables(observable.subscribeWith(observer)) }
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
