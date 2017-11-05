package com.sedsoftware.yaptalker.features.topic.adapter

interface ChosenTopicItemClickListener {

  fun onPostItemClicked(postId: String, isKarmaAvailable: Boolean)
}
