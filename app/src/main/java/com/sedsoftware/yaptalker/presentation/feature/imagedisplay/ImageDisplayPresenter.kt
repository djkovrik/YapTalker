package com.sedsoftware.yaptalker.presentation.feature.imagedisplay

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.ImageHelperInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class ImageDisplayPresenter @Inject constructor(
    private val imageHelperInteractor: ImageHelperInteractor
) : BasePresenter<ImageDisplayView>() {

    fun saveImage(url: String) {
        imageHelperInteractor
            .saveImage(url.validateUrl())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(event(PresenterLifecycle.DESTROY))
            .subscribe({ fileName ->
                viewState.fileSavedMessage(fileName)
            }, { e: Throwable ->
                Timber.e("Saving error: ${e.message}")
                viewState.fileNotSavedMessage()
            })
    }

    fun shareImage(url: String) {
        imageHelperInteractor
            .shareImage(url.validateUrl())
            .autoDisposable(event(PresenterLifecycle.DETACH_VIEW))
            .subscribe({
                Timber.d("Image sharing request launched.")
            }, { e: Throwable ->
                e.message?.let { viewState.showErrorMessage(it) }
            })
    }
}
