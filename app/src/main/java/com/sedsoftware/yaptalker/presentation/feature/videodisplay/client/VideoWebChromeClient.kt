package com.sedsoftware.yaptalker.presentation.feature.videodisplay.client

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnErrorListener
import android.media.MediaPlayer.OnPreparedListener
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import android.widget.VideoView

@Suppress("NestedBlockDepth", "UnsafeCallOnNullableType")
class VideoWebChromeClient : WebChromeClient, OnPreparedListener, OnCompletionListener, OnErrorListener {

    private var activityNonVideoView: View? = null
    private var activityVideoView: ViewGroup? = null
    private var loadingView: View? = null
    private var webView: VideoWebView? = null
    private var isVideoFullscreen: Boolean = false
    private var videoViewContainer: FrameLayout? = null
    private var videoViewCallback: WebChromeClient.CustomViewCallback? = null
    private var toggledFullscreenCallback: FullscreenCallback? = null

    interface FullscreenCallback {
        fun toggledFullscreen(fullscreen: Boolean)
    }

    constructor(activityNonVideoView: View, activityVideoView: ViewGroup) {
        this.activityNonVideoView = activityNonVideoView
        this.activityVideoView = activityVideoView
        this.loadingView = null
        this.webView = null
        this.isVideoFullscreen = false
    }


    constructor(activityNonVideoView: View, activityVideoView: ViewGroup, loadingView: View) {
        this.activityNonVideoView = activityNonVideoView
        this.activityVideoView = activityVideoView
        this.loadingView = loadingView
        this.webView = null
        this.isVideoFullscreen = false
    }

    constructor(
        activityNonVideoView: View, activityVideoView: ViewGroup, loadingView: View,
        webView: VideoWebView
    ) {
        this.activityNonVideoView = activityNonVideoView
        this.activityVideoView = activityVideoView
        this.loadingView = loadingView
        this.webView = webView
        this.isVideoFullscreen = false
    }

    fun setOnToggledFullscreen(callback: FullscreenCallback) {
        this.toggledFullscreenCallback = callback
    }

    override fun onShowCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
        if (view is FrameLayout) {

            val focusedChild = view.focusedChild

            this.isVideoFullscreen = true
            this.videoViewContainer = view
            this.videoViewCallback = callback

            activityNonVideoView?.visibility = View.INVISIBLE
            activityVideoView?.addView(
                videoViewContainer,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )
            activityVideoView?.visibility = View.VISIBLE

            if (focusedChild is VideoView) {
                focusedChild.setOnPreparedListener(this)
                focusedChild.setOnCompletionListener(this)
                focusedChild.setOnErrorListener(this)
            } else {
                if (webView != null && webView!!.settings.javaScriptEnabled) {

                    var javascript = "javascript:"
                    javascript += "_ytrp_html5_video = document.getElementsByTagName('video')[0];"
                    javascript += "if (_ytrp_html5_video !== undefined) {"
                    run {
                        javascript += "function _ytrp_html5_video_ended() {"
                        run {
                            javascript += "_ytrp_html5_video.removeEventListener('ended', _ytrp_html5_video_ended);"
                            javascript += "_VideoEnabledWebView.notifyVideoEnd();"
                        }
                        javascript += "}"
                        javascript += "_ytrp_html5_video.addEventListener('ended', _ytrp_html5_video_ended);"
                    }
                    javascript += "}"
                    webView?.loadUrl(javascript)
                }
            }

            if (toggledFullscreenCallback != null) {
                toggledFullscreenCallback?.toggledFullscreen(true)
            }
        }
    }

    override fun onHideCustomView() {

        if (isVideoFullscreen) {

            activityVideoView?.visibility = View.INVISIBLE
            activityVideoView?.removeView(videoViewContainer)
            activityNonVideoView?.visibility = View.VISIBLE

            if (videoViewCallback != null) {
                videoViewCallback?.onCustomViewHidden()
            }

            isVideoFullscreen = false
            videoViewContainer = null
            videoViewCallback = null

            if (toggledFullscreenCallback != null) {
                toggledFullscreenCallback?.toggledFullscreen(false)
            }
        }
    }

    override fun getVideoLoadingProgressView(): View =
        if (loadingView != null) {
            loadingView?.visibility = View.VISIBLE
            loadingView as View
        } else {
            super.getVideoLoadingProgressView()
        }

    override fun onPrepared(mp: MediaPlayer) {
        if (loadingView != null) {
            loadingView?.visibility = View.GONE
        }
    }

    override fun onCompletion(mp: MediaPlayer) {
        onHideCustomView()
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int) = false

    fun onBackPressed(): Boolean =
        if (isVideoFullscreen) {
            onHideCustomView()
            true
        } else {
            false
        }
}
