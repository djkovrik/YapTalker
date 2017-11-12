package com.sedsoftware.yaptalker.features.topic

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.BaseTestClassForPresenters
import com.sedsoftware.yaptalker.TestKodeinInstanceRule
import com.sedsoftware.yaptalker.callProtectedPresenterMethod
import com.sedsoftware.yaptalker.getDummyTopicPage
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers

class ChosenTopicPresenterTest : BaseTestClassForPresenters() {

  @Rule
  @JvmField
  val testRule: TestKodeinInstanceRule = TestKodeinInstanceRule(testKodein)

  private val chosenTopicView = mock<ChosenTopicView>()
  private val chosenTopicViewState = mock<`ChosenTopicView$$State`>()
  private lateinit var presenter: ChosenTopicPresenter

  private val topicPage = getDummyTopicPage()

  @Before
  fun setUp() {
    whenever(yapDataManagerMock
        .getChosenTopic(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
        .thenReturn(Single.just(topicPage))

    whenever(settingsMock.isScreenAlwaysOnEnabled())
        .thenReturn(true)

    presenter = ChosenTopicPresenter()
    presenter.attachView(chosenTopicView)
    presenter.setViewState(chosenTopicViewState)
  }

  @Test
  fun onFirstViewAttachInitiatesTopicPageUpdate() {
    callProtectedPresenterMethod(presenter, "onFirstViewAttach")
    verify(chosenTopicViewState).initiateTopicLoading()
  }

  @Test
  fun normalViewAttachesDoNotInitiateTopicPageUpdate() {
    presenter.detachView(chosenTopicView)
    presenter.attachView(chosenTopicView)
    verify(chosenTopicViewState, never()).initiateTopicLoading()
    presenter.detachView(chosenTopicView)
    presenter.attachView(chosenTopicView)
    verify(chosenTopicViewState, never()).initiateTopicLoading()
  }

  @Test
  fun incorrectPageRequestShowsErrorMessage() {
    presenter.goToChosenPage(1000)
    verify(chosenTopicViewState).showCantLoadPageMessage(1000)
  }

  @Test
  fun onShareItemClickedLaunchesSharingFromView() {
    presenter.onShareItemClicked()
    verify(chosenTopicViewState).shareTopic("", 0)
  }

  @Test
  fun attachAndDetachViewHandleScreenAwakeIfEnabled() {
    presenter.attachView(chosenTopicView)
    verify(chosenTopicViewState).blockScreenSleep()
    presenter.detachView(chosenTopicView)
    verify(chosenTopicViewState).unblockScreenSleep()
  }
}
