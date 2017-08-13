package com.sedsoftware.yaptalker.features.forumslist

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

  fun loadForumsList() {

    val subscription =
        yapDataManager
            .getForumsList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoadingStart() }
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

  fun onLoadingStart() {
    viewState.showProgressBar()
  }

  fun onLoadingSuccess(forums: List<ForumItem>) {
    viewState.hideProgressBar()
    viewState.showForums(forums)
  }

  fun onLoadingError(error: Throwable) {
    viewState.hideProgressBar()
    error.message?.let { viewState.showErrorMessage(it) }
  }
}