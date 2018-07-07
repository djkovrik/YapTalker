package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class MailInboxPageParsed {
    @Selector("tr[id^=r_m_]")
    var letters: List<MailLetterItemParsed> = emptyList()
}
