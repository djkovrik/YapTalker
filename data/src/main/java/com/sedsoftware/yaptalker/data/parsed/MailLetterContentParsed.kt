package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class MailLetterContentParsed {
    @Selector(value = "a.user-link", defValue = "")
    lateinit var authorNickname: String
    @Selector(value = "a.user-link", attr = "href", defValue = "")
    lateinit var authorLink: String
    @Selector(value = "img[src~=userpic]", attr = "src", defValue = "")
    lateinit var authorAvatar: String
    @Selector(value = "span.sub-text", defValue = "")
    lateinit var dateString: String
    @Selector(value = "div.block-text-middle", attr = "outerHtml", defValue = "")
    lateinit var letterContent: String
}
