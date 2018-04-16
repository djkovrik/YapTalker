package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed topic page in data layer.
 */
class TopicPageParsed {
  @Selector("h1.subpage > a.subtitle", defValue = "Unknown")
  lateinit var topicTitle: String
  @Selector("td.bottommenu > font", defValue = "")
  lateinit var isClosed: String
  @Selector("input[name~=auth_key]", attr = "outerHtml", format = "value=\"([a-z0-9]+)\"", defValue = "")
  lateinit var authKey: String
  @Selector("div.rating-value", format = "([-\\d]+)", defValue = "0")
  lateinit var topicRating: String
  @Selector("div[rel=rating] img[src$=rating-cell-minus.gif]", attr = "src", defValue = "")
  lateinit var topicRatingPlusAvailable: String
  @Selector("div[rel=rating] img[src$=rating-cell-plus.gif]", attr = "src", defValue = "")
  lateinit var topicRatingMinusAvailable: String
  @Selector("div[rel=rating] img[src$=rating-cell-plus-clicked.gif]", attr = "src", defValue = "")
  lateinit var topicRatingPlusClicked: String
  @Selector("div[rel=rating] img[src$=rating-cell-minus-clicked.gif]", attr = "src", defValue = "")
  lateinit var topicRatingMinusClicked: String
  @Selector(
    value = "div[rel=rating] a[onclick~=doRatePost]",
    format = "(?<=\\d{2}, )(\\d+)((?=, ))",
    attr = "outerHtml",
    defValue = "0"
  )
  lateinit var topicRatingTargetId: String
  @Selector("table.row3")
  lateinit var navigation: TopicNavigationPanel
  @Selector("table[id~=p_row_\\d+]:has(.normalname)")
  lateinit var posts: List<PostItemParsed>
}
