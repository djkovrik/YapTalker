package com.sedsoftware.yaptalker.features.forumslist

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.features.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class ForumsPresenter : BasePresenter<ForumsView>() {

  @Inject
  lateinit var yapDataManager: YapDataManager

  init {
    YapTalkerApp.appComponent.inject(this)
  }

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

  fun checkSavedState(savedViewState: Bundle?, key: String) {
    if (savedViewState != null && savedViewState.containsKey(key)) {
      val forums = savedViewState.getParcelableArrayList<ForumItem>(key)
      viewState.showForums(forums)
    } else {
      loadForumsList()
    }
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
}
