package com.sedsoftware.yaptalker.features.forumslist

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.features.base.BasePresenter
import com.sedsoftware.yaptalker.features.base.events.PresenterLifecycle
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ForumsPresenter : BasePresenter<ForumsView>() {

  @Suppress("MagicNumber")
  companion object {
    private val nsfwForumSections = setOf(4, 33, 36)
  }

  fun checkSavedState(savedViewState: Bundle?, key: String) {
    if (savedViewState != null && savedViewState.containsKey(key)) {
      val items = savedViewState.getParcelableArrayList<ForumItem>(key)
      onRestoringSuccess(items)
    } else {
      loadForumsList()
    }
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
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // OnNext
          forumsListItem ->
          onLoadingSuccess(forumsListItem)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  private fun onLoadingSuccess(item: ForumItem) {
    viewState.appendForumItem(item)
  }

  private fun onRestoringSuccess(items: List<ForumItem>) {
    viewState.appendForumsList(items)
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
