package com.sedsoftware.yaptalker.presentation.feature.userprofile

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.userprofile.GetUserProfile
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.mapper.UserProfileModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.UserProfileModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class UserProfilePresenter @Inject constructor(
  private val getUserProfileUseCase: GetUserProfile,
  private val userProfileModelMapper: UserProfileModelMapper
) : BasePresenter<UserProfileView>() {

  fun loadUserProfile(profileId: Int) {
    getUserProfileUseCase
      .execute(GetUserProfile.Params(profileId))
      .subscribeOn(Schedulers.io())
      .map(userProfileModelMapper)
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe(getUserProfileObserver())
  }

  private fun getUserProfileObserver() =
    object : SingleObserver<YapEntity> {

      override fun onSuccess(profile: YapEntity) {
        profile as UserProfileModel
        viewState.displayProfile(profile)
        viewState.updateCurrentUiState(profile.nickname)
        Timber.i("User profile loaded successfully.")
      }

      override fun onSubscribe(d: Disposable) {
      }

      override fun onError(e: Throwable) {
        e.message?.let { viewState.showErrorMessage(it) }
      }
    }
}
