package com.sedsoftware.yaptalker.features.forumslist

import com.arellomobile.mvp.InjectViewState
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.features.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class ForumsPresenter : BasePresenter<ForumsView>() {

  init {
    YapTalkerApp.appComponent.inject(this)
  }

  @Inject
  lateinit var yapDataManager: YapDataManager

  @Inject
  lateinit var titleChannel: BehaviorRelay<String>

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    attachRefreshIndicator(yapDataManager.requestState, {
      // onStart
      viewState.showRefreshing()
    }, {
      // onFinish
      viewState.hideRefreshing()
    })
  }

  override fun attachView(view: ForumsView?) {
    super.attachView(view)
    viewState.updateAppbarTitle()
    Timber.tag("xxxx").d("attachView from forums")
  }

  fun loadForumsList() {

    val subscription =
        yapDataManager
            .getForumsList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              // onSuccess
              forumsList: List<ForumItem> ->
              onLoadingSuccess(forumsList)
            }, {
              // onError
              throwable ->
              onLoadingError(throwable)
            })

    unsubscribeOnDestroy(subscription)
  }

  fun onLoadingSuccess(forums: List<ForumItem>) {
    viewState.showForums(forums)
  }

  fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }

  fun updateTitle(title: String) {
    pushAppbarTitle(titleChannel, title)
  }
}
