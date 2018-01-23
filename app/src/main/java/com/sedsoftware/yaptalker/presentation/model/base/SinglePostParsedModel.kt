package com.sedsoftware.yaptalker.presentation.model.base

/**
 * Class which contains single post parsed content.
 */
class SinglePostParsedModel(
  val content: MutableList<PostContentModel>,
  val images: MutableList<String>,
  val videos: MutableList<String>,
  val videosRaw: MutableList<String>
)
