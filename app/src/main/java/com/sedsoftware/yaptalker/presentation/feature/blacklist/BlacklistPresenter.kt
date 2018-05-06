package com.sedsoftware.yaptalker.presentation.feature.blacklist

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.entity.base.BlacklistedTopic
import com.sedsoftware.yaptalker.domain.interactor.BlacklistInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.feature.blacklist.adapter.BlacklistElementsClickListener
import com.sedsoftware.yaptalker.presentation.mapper.BlacklistTopicModelMapper
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class BlacklistPresenter @Inject constructor(
  private val router: Router,
  private val blacklistInteractor: BlacklistInteractor,
  private val topicsMapper: BlacklistTopicModelMapper
) : BasePresenter<BlacklistView>(), BlacklistElementsClickListener {

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadBlacklist()
  }

  override fun attachView(view: BlacklistView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
  }

  override fun onDeleteIconClick(topicId: Int) {
    viewState.showDeleteConfirmationDialog(topicId)
  }

  fun deleteTopicFromBlacklist(topicId: Int) {
    blacklistInteractor
      .removeTopicFromBlacklistById(topicId)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({
        Timber.i("Topic deleted from blacklist")
        loadBlacklist()
      }, { error ->
        error.message?.let { viewState.showErrorMessage(it) }
      })
  }

  private fun loadBlacklist() {
    blacklistInteractor
      .getBlacklistedTopics()
      .subscribeOn(Schedulers.io())
      .map(topicsMapper)
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ topics ->
        viewState.showBlacklistedTopics(topics)
      }, { error ->
        error.message?.let { viewState.showErrorMessage(it) }
      })
  }
}
