package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed incubator page in data layer.
 */
class IncubatorPageParsed {
  @Selector("td.newshead")
  lateinit var headers: List<IncubatorHead>
  @Selector("td.postcolor")
  lateinit var contents: List<IncubatorContent>
  @Selector("td.holder")
  lateinit var bottoms: List<IncubatorBottom>
}
