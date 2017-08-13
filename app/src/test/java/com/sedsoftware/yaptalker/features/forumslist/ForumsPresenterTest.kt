package com.sedsoftware.yaptalker.features.forumslist

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.TestComponent
import com.sedsoftware.yaptalker.TestComponentRule
import com.sedsoftware.yaptalker.data.remote.yap.YapDataManager
import com.sedsoftware.yaptalker.di.ApplicationComponent
import com.sedsoftware.yaptalker.features.news.NewsPresenterTest
import com.sedsoftware.yaptalker.getDummyForumsList
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForumsPresenterTest {

  companion object {
    val forums = getDummyForumsList()
    val error = RuntimeException("Test error")
  }

  @Rule
  @JvmField
  val testComponentRule: TestComponentRule = TestComponentRule(testAppComponent())

  val dataManagerMock = mock<YapDataManager>()
  val forumsView = mock<ForumsView>()
  val forumsViewState = mock<`ForumsView$$State`>()

  lateinit var presenter: ForumsPresenter

  @Before
  fun setUp() {
    presenter = ForumsPresenter()
    presenter.attachView(forumsView)
    presenter.setViewState(forumsViewState)

    RxAndroidPlugins.reset()
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
  }

  @Test
  fun forums_presenterShouldDisplayProgressBarOnLoadingStart() {

    whenever(dataManagerMock.getForumsList())
        .thenReturn(Single.just(forums))

    presenter.loadForumsList()
    verify(forumsViewState).showProgressBar()
  }

  @Test
  fun forums_presenterShouldDisplayForumsOnSuccess() {

    presenter.onLoadingSuccess(forums)
    verify(forumsViewState).hideProgressBar()
    verify(forumsViewState).showForums(forums)
  }

  @Test
  fun forums_presenterShouldDisplayErrorMessageOnError() {

    presenter.onLoadingError(error)
    verify(forumsViewState).showErrorMessage(NewsPresenterTest.error.message!!)
  }

  fun testAppComponent(): ApplicationComponent {
    return object : TestComponent() {
      override fun inject(presenter: ForumsPresenter) {
        presenter.yapDataManager = dataManagerMock
      }
    }
  }
}