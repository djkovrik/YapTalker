package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents news item in domain layer.
 */
class NewsItem(
  val title: String,
  val link: String,
  val rating: Int,
  val description: String,
  val images: List<String>,
  val videos: List<String>,
  val videosRaw: List<String>,
  val author: String,
  val authorLink: String,
  val date: String,
  val forumName: String,
  val forumLink: String,
  val comments: Int,
  val cleanedDescription: String,
  val isYapLink: Boolean
) : BaseEntity
