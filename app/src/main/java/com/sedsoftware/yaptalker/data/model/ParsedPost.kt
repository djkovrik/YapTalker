package com.sedsoftware.yaptalker.data.model

import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

class ParsedPost(html: String,
    val content: MutableList<Content> = ArrayList(),
    val images: MutableList<String> = ArrayList(),
    val videos: MutableList<String> = ArrayList(),
    val videosRaw: MutableList<String> = ArrayList()) {

  companion object {
    private const val TEXT_SELECTOR = "postcolor"
    private const val RATING_SELECTOR = "div[rel=rating]"
    private const val EDITED_TIME_SELECTOR = "span.edit"
    private const val CLIENT_SELECTOR = "span[style~=grey]"
    private const val EMOTICON_SRC_SELECTOR = "[src*=emoticons]"
    private const val EMOTICON_SELECTOR = "emoticons"
    private const val QUOTE_SELECTOR = "QUOTE"
    private const val SPOILER_SELECTOR = "SPOILER"
    private const val QUOTE_START_TEXT = "Цитата"
    private const val IFRAME_TAG = "iframe"
    private const val IMG_TAG = "img"
    private const val TD_TAG = "td"
    private const val A_TAG = "a"
    private const val SRC_ATTR = "src"
    private const val HREF_ATTR = "href"
    private const val QUOTE_AUTHOR_MARKER = "@"
    private const val QUOTE_MARKER = "Цитата"

    private val tagsToSkip =
        setOf("#root", "html", "head", "body", "table", "tbody", "tr", "br", "b", "i", "u")
    private val attrsToSkip = setOf("rating", "clear")
    private val contentWhitelist: Whitelist = Whitelist()
        .addTags("i", "u", "b", "br", "img")
        .addAttributes("img", "src")
  }

  init {
    val singlePost = Jsoup.parse(html)

    singlePost
        .allElements
        .filter { element -> !tagsToSkip.contains(element.tagName()) }
        .filter { element ->
          var notSkip = true
          attrsToSkip.forEach attrs@ { attribute ->
            if (element.attributes().toString().contains(attribute)) {
              notSkip = false
              return@attrs
            }
          }
          notSkip
        }
        .forEach { element ->
          // Texts
          if (element.hasClass(TEXT_SELECTOR)) {
            element.select(RATING_SELECTOR).remove()
            element.select(EDITED_TIME_SELECTOR).remove()
            element.select(CLIENT_SELECTOR).remove()
            element.select(IMG_TAG).not(EMOTICON_SRC_SELECTOR).remove()

            element.html().cleanExtraTags().trimLinebreakTags().apply {
              if (this.isNotEmpty())
                content.add(PostText(text = this))
            }
          }

          // Quotes
          if (element.attributes().toString().contains(QUOTE_SELECTOR)
              && !element.text().contains(QUOTE_START_TEXT)) {
            element.html().cleanExtraTags().trimLinebreakTags().apply {
              if (this.isNotEmpty())
                content.add(PostQuote(text = this))
            }
          }

          // Quote authors
          if (element.text().contains(QUOTE_AUTHOR_MARKER) &&
              !element.html().contains(Regex("[\\r\\n]+"))) {
            content.add(PostQuoteAuthor(text = element.html()))
          } else if (element.text() == QUOTE_MARKER) {
            content.add(PostQuoteAuthor(text = element.html()))
          }

          // Spoilers
          if (element.tagName() == TD_TAG &&
              element.attributes().toString().contains(SPOILER_SELECTOR)) {
            element.html().cleanExtraTags().trimLinebreakTags().apply {
              if (this.isNotEmpty())
                content.add(PostHiddenText(text = this))
            }
          }

          // Images
          if (element.tagName() == IMG_TAG &&
              element.hasAttr(SRC_ATTR) &&
              !element.attr(SRC_ATTR).contains(EMOTICON_SELECTOR)) {
            images.add(element.attr(SRC_ATTR))
          }

          // Videos
          if (element.tagName() == IFRAME_TAG &&
              element.hasAttr(SRC_ATTR)) {
            videos.add(element.attr(SRC_ATTR))
            videosRaw.add(element.toString())
          }

          // P.S.
          if (element.attributes().toString().contains("edit")) {
            content.add(PostScript(
                text = element.html()))
          }

          // Links
          if (element.tagName() == A_TAG && element.text().isNotEmpty()) {
            content.add(PostLink(
                url = element.attr(HREF_ATTR),
                title = element.text()))
          }
        }
  }

  private fun String.cleanExtraTags(): String {
    return Jsoup
        .clean(this, contentWhitelist)
        // Replace html spaces
        .replace("&nbsp;", " ")
        // Replace extra <br>
        //.replace(Regex("(<br>(\\s+)?\\R)+", RegexOption.MULTILINE), "<br>")
  }

  private fun String.trimLinebreakTags(): String {
    return this.removePrefix("<br>").removeSuffix("<br>").trim()
  }
}
