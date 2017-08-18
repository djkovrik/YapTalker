package com.sedsoftware.yaptalker.features.forum

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.model.ForumPage
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.data.remote.yap.YapRequestState
import com.sedsoftware.yaptalker.features.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class ChosenForumPresenter : BasePresenter<ChosenForumView>() {

  companion object {
    private const val LAST_UPDATE_SORTER = "last_post"
    private const val RATING_SORTER = "rank"
  }

  @Inject
  lateinit var yapDataManager: YapDataManager

  private var currentForumId = 0
  private var currentPage = 0
  private var currentSorting = LAST_UPDATE_SORTER

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    attachRefreshIndicator()
  }

  fun loadForum(forumId: Int, page: Int = 0, sortByRank: Boolean = false) {
    currentForumId = forumId
    currentPage = page
    currentSorting =
        if (sortByRank) RATING_SORTER
        else LAST_UPDATE_SORTER

    loadForumCurrentPage()
  }

  private fun loadForumCurrentPage() {
    val subscription =
        yapDataManager
            .getChosenForum(currentForumId, currentPage, currentSorting)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              // onSuccess
              page: ForumPage ->
              onLoadingSuccess(page)
            }, {
              // onError
              throwable ->
              onLoadingError(throwable)
            })

    unsubscribeOnDestroy(subscription)
  }

  private fun attachRefreshIndicator() {
    val subscription =
        yapDataManager.requestState.subscribe { state: Long ->
          when (state) {
            YapRequestState.LOADING -> {
              viewState.showRefreshing()
            }
            YapRequestState.COMPLETED,
            YapRequestState.ERROR -> {
              viewState.hideRefreshing()
            }
          }
        }

    unsubscribeOnDestroy(subscription)
  }

  private fun onLoadingSuccess(forumPage: ForumPage) {
    viewState.refreshTopics(forumPage.topics)
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
