package com.sedsoftware.yaptalker.features.forumslist

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.parsing.ForumItem
import com.sedsoftware.yaptalker.base.navigation.NavigationScreens
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class ForumsPresenter : BasePresenter<ForumsView>() {

  @Suppress("MagicNumber")
  companion object {
    private val nsfwForumSections = setOf(4, 33, 36)
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

  fun navigateToChosenForum(forumId: Int) {
    router.navigateTo(NavigationScreens.CHOSEN_FORUM_SCREEN, forumId)
  }

  private fun onLoadingSuccess(item: ForumItem) {
    viewState.appendForumItem(item)
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
