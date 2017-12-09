package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.executor.ExecutionThread
import com.sedsoftware.yaptalker.domain.executor.PostExecutionThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

abstract class UseCase<T, in Params>(
    private val executionThread: ExecutionThread,
    private val postExecutionThread: PostExecutionThread) {

  abstract fun buildUseCaseObservable(params: Params): Observable<T>

  private val disposables by lazy { CompositeDisposable() }

  fun execute(observer: DisposableObserver<T>, params: Params) {

    requireNotNull(observer)

    buildUseCaseObservable(params)
        .subscribeOn(executionThread.getScheduler())
        .observeOn(postExecutionThread.getScheduler())
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
