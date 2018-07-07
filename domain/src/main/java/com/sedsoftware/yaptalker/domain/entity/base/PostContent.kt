package com.sedsoftware.yaptalker.domain.entity.base

/**
 * Class contains all possible content types for SinglePost.
 */
sealed class PostContent {
    class PostText(val text: String) : PostContent()
    class PostQuote(val text: String) : PostContent()
    class PostQuoteAuthor(val text: String) : PostContent()
    class PostHiddenText(val text: String) : PostContent()
    class PostScript(val text: String) : PostContent()
    class PostWarning(val text: String) : PostContent()
}
