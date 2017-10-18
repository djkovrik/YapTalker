package com.sedsoftware.yaptalker.features.userprofile

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenterWithLoading
import com.sedsoftware.yaptalker.base.events.PresenterLifecycle
import com.sedsoftware.yaptalker.data.model.UserProfile
import com.uber.autodispose.kotlin.autoDisposeWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class UserProfilePresenter : BasePresenterWithLoading<UserProfileView>() {

  fun loadUserProfile(profileId: Int) {
    yapDataManager
        .getUserProfile(profileId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposeWith(event(PresenterLifecycle.DESTROY))
        .subscribe({
          // onSuccess
          profile: UserProfile ->
          onLoadingSuccess(profile)
        }, {
          // onError
          throwable ->
          onLoadingError(throwable)
        })
  }

  override fun onLoadingStart() {

  }

  override fun onLoadingFinish() {

  }

  private fun onLoadingSuccess(profile: UserProfile) {
    viewState.displayProfile(profile)
  }

  private fun onLoadingError(error: Throwable) {
    error.message?.let { viewState.showErrorMessage(it) }
  }
}
