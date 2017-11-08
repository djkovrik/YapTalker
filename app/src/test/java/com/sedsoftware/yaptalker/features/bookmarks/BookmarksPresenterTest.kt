package com.sedsoftware.yaptalker.features.bookmarks

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sedsoftware.yaptalker.BaseTestClassForPresenters
import com.sedsoftware.yaptalker.TestKodeinInstanceRule
import com.sedsoftware.yaptalker.getDummyBookmarks
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookmarksPresenterTest : BaseTestClassForPresenters() {

  @Rule
  @JvmField
  val testRule: TestKodeinInstanceRule = TestKodeinInstanceRule(testKodein)

  private val bookmarksView = mock<BookmarksView>()
  private val bookmarksViewState = mock<`BookmarksView$$State`>()
  private lateinit var presenter: BookmarksPresenter

  @Before
  fun setUp() {
    whenever(yapDataManagerMock.getBookmarks())
        .thenReturn(Single.just(getDummyBookmarks()))

    presenter = BookmarksPresenter()
    presenter.attachView(bookmarksView)
    presenter.setViewState(bookmarksViewState)
  }

  @Test
  fun onDeleteIconClickedShowsConfirmationDialog() {
    presenter.onDeleteIconClicked(1)
    verify(bookmarksViewState).showDeleteConfirmDialog(1)
  }
}
