package com.sedsoftware.yaptalker.features.videodisplay

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter

@InjectViewState
class VideoDisplayPresenter : BasePresenter<VideoDisplayView>() {

  fun loadVideoContent() {
    viewState.displayWebViewContent()
  }

  override fun attachView(view: VideoDisplayView?) {
    super.attachView(view)
    viewState.initWebView()
  }

  override fun detachView(view: VideoDisplayView?) {
    viewState.clearWebView()
    super.detachView(view)
  }
}
