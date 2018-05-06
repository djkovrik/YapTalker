package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed topic page in data layer.
 */
class TopicPageParsed {
  @Selector(value = "h1.subpage > a.subtitle", defValue = "Unknown")
  lateinit var topicTitle: String
  @Selector(value = "td.bottommenu > font", defValue = "")
  lateinit var isClosed: String
  @Selector(value = "input[name~=auth_key]", attr = "outerHtml", regex = "value=\"([a-z0-9]+)\"", defValue = "")
  lateinit var authKey: String
  @Selector(value = "div.rating-value", regex = "([-\\d]+)", defValue = "0")
  lateinit var topicRating: String
  @Selector(value = "div[rel=rating] img[src$=rating-cell-minus.gif]", attr = "src", defValue = "")
  lateinit var topicRatingPlusAvailable: String
  @Selector(value = "div[rel=rating] img[src$=rating-cell-plus.gif]", attr = "src", defValue = "")
  lateinit var topicRatingMinusAvailable: String
  @Selector(value = "div[rel=rating] img[src$=rating-cell-plus-clicked.gif]", attr = "src", defValue = "")
  lateinit var topicRatingPlusClicked: String
  @Selector(value = "div[rel=rating] img[src$=rating-cell-minus-clicked.gif]", attr = "src", defValue = "")
  lateinit var topicRatingMinusClicked: String
  @Selector(
    value = "div[rel=rating] a[onclick~=doRatePost]",
    regex = "(?<=\\d{2}, )(\\d+)((?=, ))",
    attr = "outerHtml",
    defValue = "0"
  )
  lateinit var topicRatingTargetId: String
  @Selector(value = "table.row3")
  lateinit var navigation: TopicNavigationPanel
  @Selector(value = "table[id~=p_row_\\d+]:has(.normalname)")
  lateinit var posts: List<PostItemParsed>
}
