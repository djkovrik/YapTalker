package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed incubator page in data layer.
 */
class IncubatorPageParsed {
  @Selector(value = "td.newshead")
  lateinit var headers: List<IncubatorHead>
  @Selector(value = "td.postcolor")
  lateinit var contents: List<IncubatorContent>
  @Selector(value = "td.holder")
  lateinit var bottoms: List<IncubatorBottom>
}
