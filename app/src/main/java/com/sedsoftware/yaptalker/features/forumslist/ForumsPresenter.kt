package com.sedsoftware.yaptalker.features.forumslist

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.features.base.BasePresenter

@InjectViewState
class ForumsPresenter : BasePresenter<ForumsView>() {


//  @Inject
//  lateinit var yapDataManager: YapDataManager
//
//  @Inject
//  lateinit var titleChannel: BehaviorRelay<String>

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

//    attachRefreshIndicator(yapDataManager.requestState, {
//      // onStart
//      viewState.showRefreshing()
//    }, {
//      // onFinish
//      viewState.hideRefreshing()
//    })
  }

  override fun attachView(view: ForumsView?) {
    super.attachView(view)
    viewState.updateAppbarTitle()
  }

  fun loadForumsList() {

    viewState.clearForumsList()

//    yapDataManager
//        .getForumsList()
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe({
//          // OnNext
//          forumsListItem: ForumItem ->
//          onLoadingSuccess(forumsListItem)
//        }, {
//          // onError
//          throwable ->
//          onLoadingError(throwable)
//        })
//        .apply { unsubscribeOnDestroy(this) }
  }

  fun updateTitle(title: String) {
//    pushAppbarTitle(titleChannel, title)
  }

  private fun onLoadingSuccess(item: ForumItem) {
    viewState.appendForumItem(item)
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
