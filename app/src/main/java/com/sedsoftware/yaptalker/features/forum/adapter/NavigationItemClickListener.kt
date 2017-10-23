package com.sedsoftware.yaptalker.features.forum.adapter

interface NavigationItemClickListener {

  fun onGoToFirstPageClick()

  fun onGoToLastPageClick()

  fun onGoToPreviousPageClick()

  fun onGoToNextPageClick()

  fun onGoToSelectedPageClick()
}
