package com.sedsoftware.yaptalker.presentation.features.topic.adapter

interface ChosenTopicItemClickListener {

  fun onPostItemClicked(postId: Int, isKarmaAvailable: Boolean)
}
