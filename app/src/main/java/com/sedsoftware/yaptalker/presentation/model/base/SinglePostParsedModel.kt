package com.sedsoftware.yaptalker.presentation.model.base

class SinglePostParsedModel(
  val content: MutableList<PostContentModel>,
  val images: MutableList<String>,
  val videos: MutableList<String>,
  val videosRaw: MutableList<String>,
  val videoTypes: List<String>
)
