package com.sedsoftware.yaptalker.presentation.feature.userprofile

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.interactor.UserProfileInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.mapper.UserProfileModelMapper
import com.sedsoftware.yaptalker.presentation.model.base.UserProfileModel
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class UserProfilePresenter @Inject constructor(
    private val userProfileInteractor: UserProfileInteractor,
    private val userProfileModelMapper: UserProfileModelMapper,
    private val schedulers: SchedulersProvider
) : BasePresenter<UserProfileView>() {

    fun loadUserProfile(profileId: Int) {
        userProfileInteractor
            .getUserProfile(profileId)
            .map(userProfileModelMapper)
            .observeOn(schedulers.ui())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe(getUserProfileObserver())
    }

    private fun getUserProfileObserver() =
        object : SingleObserver<UserProfileModel> {

            override fun onSuccess(profile: UserProfileModel) {
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
