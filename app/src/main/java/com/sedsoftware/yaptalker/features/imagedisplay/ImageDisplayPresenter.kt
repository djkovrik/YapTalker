package com.sedsoftware.yaptalker.features.imagedisplay

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.features.base.BasePresenter

@InjectViewState
class ImageDisplayPresenter : BasePresenter<ImageDisplayView>() {

  fun toggleFullscreenView() {
    viewState.toggleSystemUiVisibility()
  }

  fun toggleAppbarVisibility(visible: Boolean) {
    if (visible) {
      viewState.showAppbar()
    } else {
      viewState.hideAppbar()
    }
  }
}