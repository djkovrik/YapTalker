package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents edited post text in data layer.
 */
class EditedPostParsed {
    @Selector(value = "textarea[name=Post].textinput", defValue = "")
    lateinit var editedText: String
}
