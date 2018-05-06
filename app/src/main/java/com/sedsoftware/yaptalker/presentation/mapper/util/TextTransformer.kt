package com.sedsoftware.yaptalker.presentation.mapper.util

import android.content.Context
import android.text.Html
import android.text.Spanned
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.quantityString
import com.sedsoftware.yaptalker.presentation.extensions.string
import java.util.Locale
import javax.inject.Inject

class TextTransformer @Inject constructor(private val context: Context) {

  companion object {
    private const val POSITIVE_RATING = "<font color='#4CAF50'>+%d</font>"
    private const val NEGATIVE_RATING = "<font color='#E57373'>%d</font>"
    private const val ZERO_RATING = "<font color='#BDBDBD'>%d</font>"
  }

  private val forumTitleTemplate: String by lazy {
    context.string(R.string.news_forum_title_template)
  }

  private val pinnedTopicTemplate: String by lazy {
    context.getString(R.string.forum_pinned_topic_template)
  }

  private val closedTopicTemplate: String by lazy {
    context.getString(R.string.forum_closed_topic_template)
  }

  private val pinnedAndClosedTopicTemplate: String by lazy {
    context.getString(R.string.forum_pinned_and_closed_topic_template)
  }

  private val commentsTemplate: String by lazy {
    context.getString(R.string.forum_comments_template)
  }

  private val commentsTemplateShort: String by lazy {
    context.getString(R.string.news_comments_template_short)
  }

  private val websiteTemplate: String by lazy {
    context.string(R.string.profile_web_site)
  }

  private val pagesLabelTemplate: String by lazy {
    context.getString(R.string.navigation_pages_template)
  }

  private val blacklistedDateTemplate: String by lazy {
    context.getString(R.string.title_blacklisted_date)
  }

  @Suppress("DEPRECATION")
  fun transformHtmlToSpanned(html: String): Spanned =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
      Html.fromHtml(html)
    }

  @Suppress("DEPRECATION")
  fun transformWebsiteToSpanned(link: String): Spanned {
    val html = if (link.startsWith("http")) {
      String.format(Locale.getDefault(), websiteTemplate, link)
    } else {
      link
    }

    return transformHtmlToSpanned(html)
  }

  @Suppress("DEPRECATION")
  fun transformRankToFormattedText(rank: Int): Spanned {
    val html = when {
      rank > 0 -> {
        String.format(Locale.getDefault(), POSITIVE_RATING, rank)
      }
      rank < 0 -> {
        String.format(Locale.getDefault(), NEGATIVE_RATING, rank)
      }
      else -> {
        String.format(Locale.getDefault(), ZERO_RATING, rank)
      }
    }

    return transformHtmlToSpanned(html)
  }

  fun transformCommentsLabel(comments: Int): String {
    val commentsTemplate: String = context.quantityString(R.plurals.news_comments_template, comments)
    return String.format(Locale.getDefault(), commentsTemplate, comments)
  }

  fun transformBlacklistDate(dateString: String): String =
    String.format(Locale.getDefault(), blacklistedDateTemplate, dateString)

  fun transformCommentsLabelShort(comments: Int): String =
    String.format(Locale.getDefault(), commentsTemplateShort, comments)

  fun transformNewsForumTitle(title: String): String =
    String.format(Locale.getDefault(), forumTitleTemplate, title)

  fun createNavigationLabel(currentPage: Int, totalPages: Int): String =
    String.format(Locale.getDefault(), pagesLabelTemplate, currentPage, totalPages)

  fun createCommentsLabel(comments: Int): String =
    String.format(Locale.getDefault(), commentsTemplate, comments)

  fun createForumTopicTitle(isPinned: Boolean, isClosed: Boolean, title: String): String = when {
    isPinned && isClosed -> String.format(Locale.getDefault(), pinnedAndClosedTopicTemplate, title)
    isPinned && !isClosed -> String.format(Locale.getDefault(), pinnedTopicTemplate, title)
    !isPinned && isClosed -> String.format(Locale.getDefault(), closedTopicTemplate, title)
    else -> title
  }
}
