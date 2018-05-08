package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class PostItemParsed {
  @Selector(value = ".normalname", defValue = "Unknown")
  lateinit var authorNickname: String
  @Selector(value = "a[title=Профиль]", attr = "href", defValue = "")
  lateinit var authorProfile: String
  @Selector(value = "a[title=Профиль] img", attr = "src", defValue = "//www.yaplakal.com/html/static/noavatar.gif")
  lateinit var authorAvatar: String
  @Selector(value = "div[align=left][style=padding-left:5px]", regex = "Сообщений: ([-\\d]+)", defValue = "0")
  lateinit var authorMessagesCount: String
  @Selector(value = "a.anchor", defValue = "")
  lateinit var postDate: String
  @Selector(value = "span[class~=rank-\\w+]", regex = "([-\\d]+)", defValue = "0")
  lateinit var postRank: String
  @Selector(value = "a.post-plus", attr = "innerHtml", defValue = "")
  lateinit var postRankPlusAvailable: String
  @Selector(value = "a.post-minus", attr = "innerHtml", defValue = "")
  lateinit var postRankMinusAvailable: String
  @Selector(value = "span.post-plus-clicked", attr = "innerHtml", defValue = "")
  lateinit var postRankPlusClicked: String
  @Selector(value = "span.post-minus-clicked", attr = "innerHtml", defValue = "")
  lateinit var postRankMinusClicked: String
  @Selector(value = "td[width*=100%][valign*=top]", attr = "innerHtml", defValue = "")
  lateinit var postContent: String
  @Selector(value = "a[name~=entry]", attr = "outerHtml", regex = "entry(\\d+)", defValue = "0")
  lateinit var postId: String
  @Selector(value = "a:containsOwn(цитировать)", attr = "href", defValue = "")
  lateinit var hasQuoteButton: String
  @Selector(value = "a:containsOwn(правка)", attr = "href", defValue = "")
  lateinit var hasEditButton: String
}
