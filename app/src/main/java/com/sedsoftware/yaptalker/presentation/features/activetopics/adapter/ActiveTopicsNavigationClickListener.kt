package com.sedsoftware.yaptalker.presentation.features.activetopics.adapter

interface ActiveTopicsNavigationClickListener {

  fun onGoToFirstPageClick()

  fun onGoToLastPageClick()

  fun onGoToPreviousPageClick()

  fun onGoToNextPageClick()

  fun onGoToSelectedPageClick()
}
