package com.sedsoftware.yaptalker.presentation.features.forum.adapter

interface ChosenForumElementsClickListener {

  fun onTopicClick(topicId: Int)

  fun onGoToFirstPageClick()

  fun onGoToLastPageClick()

  fun onGoToPreviousPageClick()

  fun onGoToNextPageClick()

  fun onGoToSelectedPageClick()
}
