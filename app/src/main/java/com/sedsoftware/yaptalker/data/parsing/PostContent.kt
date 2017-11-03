package com.sedsoftware.yaptalker.data.parsing

interface Content

class PostText(val text: String) : Content

class PostQuote(val text: String) : Content

class PostQuoteAuthor(val text: String) : Content

class PostHiddenText(val text: String) : Content

class PostScript(val text: String) : Content

class PostWarning(val text: String) : Content

class PostLink(val url: String, val title: String) : Content {

  override fun equals(other: Any?): Boolean {
    return when (other) {
      !is PostLink -> false
      else -> this.url == other.url
    }
  }

  override fun hashCode(): Int {
    return this.url.hashCode()
  }

  override fun toString(): String {
    return "PostLink(url='$url', title='$title')"
  }
}
