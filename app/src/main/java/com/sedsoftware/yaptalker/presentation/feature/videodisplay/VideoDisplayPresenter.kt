package com.sedsoftware.yaptalker.presentation.feature.videodisplay

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class VideoDisplayPresenter @Inject constructor(
    private val settings: Settings
) : BasePresenter<VideoDisplayView>() {

    fun checkScreenOrientation() {
        if (settings.isVideoLandscapeEnabled()) {
            viewState.forceLandscapeOrientation()
        }
    }

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
