package com.sedsoftware.yaptalker.data.network.site

import com.sedsoftware.yaptalker.data.parsed.MailInboxPageParsed
import com.sedsoftware.yaptalker.data.parsed.MailLetterContentParsed
import com.sedsoftware.yaptalker.data.parsed.MailSentPageParsed
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface YapLoaderAlpha {

    /**
     * Load received mails list from the site.
     *
     * @return Inbox page letters list.
     */
    @GET("/mail/")
    fun getMailInbox(): Single<MailInboxPageParsed>


    /**
     * Load sent mails list from the site.
     *
     * @return Sent page letters list.
     */
    @GET("/mail/sent/")
    fun getMailSent(): Single<MailSentPageParsed>


    /**
     * Load particular letter.
     *
     * @param letterId Letter uinque id.
     *
     * @return Parsed letter content.
     */
    @GET("/mail/{letterId}")
    fun getLetter(
        @Path("letterId") letterId: Int
    ): Single<MailLetterContentParsed>
}
