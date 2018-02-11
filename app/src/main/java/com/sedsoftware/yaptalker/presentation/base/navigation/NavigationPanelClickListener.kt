package com.sedsoftware.yaptalker.presentation.base.navigation

interface NavigationPanelClickListener {

  fun onGoToFirstPageClick()

  fun onGoToLastPageClick()

  fun onGoToPreviousPageClick()

  fun onGoToNextPageClick()

  fun onGoToSelectedPageClick()
}
