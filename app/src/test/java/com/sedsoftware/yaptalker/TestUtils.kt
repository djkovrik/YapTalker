package com.sedsoftware.yaptalker

import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.data.model.News
import com.sedsoftware.yaptalker.data.model.NewsItem

fun getDummyForumsList(): List<ForumItem> {
  return listOf(
      getDummyForumItem(1),
      getDummyForumItem(2),
      getDummyForumItem(3),
      getDummyForumItem(4),
      getDummyForumItem(5)
  )
}

fun getDummyForumItem(seed: Int): ForumItem {
  return ForumItem(
      title = "title$seed",
      forumId = seed + 1,
      lastTopicTitle = "title$seed",
      lastTopicAuthor = "author$seed",
      htmlDesc = "htmlDesc")
}

fun getDummyNewsList(): List<NewsItem> {
  return listOf(
      getDummyNewsItem(1),
      getDummyNewsItem(2),
      getDummyNewsItem(3),
      getDummyNewsItem(4),
      getDummyNewsItem(5))
}

fun getDummyNewsItem(seed: Int): NewsItem {
  return NewsItem(
      title = "title$seed",
      link = "link$seed",
      rating = "rating$seed",
      description = "description$seed",
      images = ArrayList(),
      videos = ArrayList(),
      author = "author$seed",
      authorLink = "link$seed",
      date = "date$seed",
      forumName = "name$seed",
      comments = "comments$seed")
}

fun getDummyNews(): News {
  return News()
}