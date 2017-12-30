package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed incubator page in data layer.
 */
class IncubatorPageParsed {
  @Selector(".newshead")
  lateinit var headers: List<IncubatorHead>
  @Selector(".news-content")
  lateinit var contents: List<IncubatorContent>
  @Selector(".newsbottom")
  lateinit var bottoms: List<IncubatorBottom>
}
