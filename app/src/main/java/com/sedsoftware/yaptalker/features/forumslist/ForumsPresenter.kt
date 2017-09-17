package com.sedsoftware.yaptalker.features.forumslist

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.features.base.BasePresenter
import com.sedsoftware.yaptalker.features.base.BasePresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ForumsPresenter : BasePresenter<ForumsView>() {

  companion object {
    private val nsfwForumSections = setOf(4, 33, 36)
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

  override fun attachView(view: ForumsView?) {
    super.attachView(view)
    viewState.updateAppbarTitle()
  }

  fun loadForumsList() {

    viewState.clearForumsList()

    yapDataManager
        .getForumsList()
        .filter { forumItem ->
          if (settings.isNsfwEnabled())
            true
          else
            !nsfwForumSections.contains(forumItem.forumId)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(BasePresenterLifecycle.DESTROY))
        .subscribe({
          // OnNext
          forumsListItem: ForumItem ->
          onLoadingSuccess(forumsListItem)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  fun updateTitle(title: String) {
    pushAppbarTitle(titleChannel, title)
  }

  private fun onLoadingSuccess(item: ForumItem) {
    viewState.appendForumItem(item)
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
