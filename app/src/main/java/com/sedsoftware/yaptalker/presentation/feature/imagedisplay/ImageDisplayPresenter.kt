package com.sedsoftware.yaptalker.presentation.feature.imagedisplay

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.imagedisplay.SaveImage
import com.sedsoftware.yaptalker.domain.interactor.imagedisplay.ShareImage
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
  private val saveImageUseCase: SaveImage,
  private val shareImageUseCase: ShareImage
) : BasePresenter<ImageDisplayView>() {

  fun saveImage(url: String) {
    saveImageUseCase
      .execute(SaveImage.Params(url.validateUrl()))
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ fileName ->
        viewState.fileSavedMessage(fileName)
      }, { _ ->
        viewState.fileNotSavedMessage()
      })
  }

  fun shareImage(url: String) {
    shareImageUseCase
      .execute(ShareImage.Params(url.validateUrl()))
      .autoDisposable(event(PresenterLifecycle.DETACH_VIEW))
      .subscribe({
        Timber.d("Image sharing request launched.")
      }, { e ->
        e.message?.let { viewState.showErrorMessage(it) }
      })
  }
}
