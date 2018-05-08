package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed active topic page in data layer.
 */
class ActiveTopicsPageParsed {
  @Selector(value = "form[name=dateline]")
  lateinit var navigation: ActiveTopicsNavigationPanel
  @Selector(value = "table tr:has(td.row4)")
  lateinit var topics: List<ActiveTopicItemParsed>
}
