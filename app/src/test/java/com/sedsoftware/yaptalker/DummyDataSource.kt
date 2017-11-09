package com.sedsoftware.yaptalker

import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsNavigationPanel
import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsPage
import com.sedsoftware.yaptalker.data.parsing.AuthorizedUserInfo
import com.sedsoftware.yaptalker.data.parsing.Bookmarks
import com.sedsoftware.yaptalker.data.parsing.ForumItem
import com.sedsoftware.yaptalker.data.parsing.ForumNavigationPanel
import com.sedsoftware.yaptalker.data.parsing.ForumPage
import com.sedsoftware.yaptalker.data.parsing.NewsItem

fun getDummyUserInfoAuthorized() = AuthorizedUserInfo("nickname", "title", "uq", "avatar")

fun getDummyUserInfoNotAuthorized() = AuthorizedUserInfo("", "", "", "")

fun getDummyNewsItem() = NewsItem(
    title = "Title",
    link = "Link",
    rating = "100",
    description = "Text",
    images = ArrayList(),
    videos = ArrayList(),
    videosRaw = ArrayList(),
    author = "author",
    authorLink = "link",
    date = "date",
    forumName = "text",
    forumLink = "link",
    comments = "100")

fun getDummyForumItem() = ForumItem(
    title = "title",
    forumId = 123,
    lastTopicTitle = "lastTopicTitle",
    lastTopicAuthor = "lastTopicAuthor",
    date = "date")

fun getDummyBookmarks() = Bookmarks()

fun getDummyActiveTopicsPage() = ActiveTopicsPage(
    navigationPanel = ActiveTopicsNavigationPanel("1", "1"),
    topics = ArrayList())

fun getDummyForumPage() = ForumPage(
    title = "title",
    id = "id",
    navigationPanel = ForumNavigationPanel("1", "1"),
    topicsList = ArrayList())
