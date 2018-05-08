package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed emoji list in data layer.
 */
class EmojiListParsed {
  @Selector(value = "tr:has(td.row1)")
  lateinit var emojis: List<EmojiParsed>
}
