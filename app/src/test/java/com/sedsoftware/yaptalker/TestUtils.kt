package com.sedsoftware.yaptalker

import com.sedsoftware.yaptalker.data.model.*

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
      id = seed + 1,
      title = "Title#$seed",
      lastTopic = TopicItemShort(
          id = seed,
          title = "Title#$seed",
          date = "date#$seed",
          author = UserProfileShort(
              id = seed + 2,
              name = "name#$seed"
          )
      )
  )
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
      summary = "News summary#$seed",
      forum = "Forum title#$seed",
      topic = TopicItemList(id = seed + 1,
          title = "Title#$seed",
          answers = seed + 2,
          uq = seed + 3,
          author = UserProfileShort(
              id = seed + 4,
              name = "Name#$seed"),
          date = "Date#$seed"))
}