package com.sedsoftware.yaptalker.presentation.features.activetopics.adapter

interface ActiveTopicsElementsClickListener {

  fun onTopicClick(forumId: Int, topicId: Int)

  fun onGoToFirstPageClick()

  fun onGoToLastPageClick()

  fun onGoToPreviousPageClick()

  fun onGoToNextPageClick()

  fun onGoToSelectedPageClick()
}
