package com.sedsoftware.yaptalker.features.videodisplay

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.sedsoftware.yaptalker.BaseTestClassForPresenters
import com.sedsoftware.yaptalker.TestKodeinInstanceRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VideoDisplayPresenterTest : BaseTestClassForPresenters() {

  @Rule
  @JvmField
  val testRule: TestKodeinInstanceRule = TestKodeinInstanceRule(testKodein)

  private val videoView = mock<VideoDisplayView>()
  private val videoViewState = mock<`VideoDisplayView$$State`>()
  private lateinit var presenter: VideoDisplayPresenter

  @Before
  fun setUp() {
    presenter = VideoDisplayPresenter()
    presenter.attachView(videoView)
    presenter.setViewState(videoViewState)
  }

  @Test
  fun loadVideoContentCallsDisplayFunc() {
    presenter.loadVideoContent()
    verify(videoViewState).displayWebViewContent()
  }

  @Test
  fun presenterAttachViewCallsVideoInit() {
    presenter.attachView(videoView)
    verify(videoViewState).initWebView()
  }

  @Test
  fun presenterDetachViewCallsVideoClean() {
    presenter.detachView(videoView)
    verify(videoViewState).clearWebView()
  }
}
