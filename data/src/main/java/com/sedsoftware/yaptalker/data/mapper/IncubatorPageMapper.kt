package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.IncubatorPageParsed
import com.sedsoftware.yaptalker.domain.entity.base.IncubatorItem
import io.reactivex.functions.Function
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import java.util.ArrayList
import javax.inject.Inject

class IncubatorPageMapper @Inject constructor() : Function<IncubatorPageParsed, List<IncubatorItem>> {

  companion object {
    private const val TOPICS_PER_PAGE = 50
  }

  override fun apply(from: IncubatorPageParsed): List<IncubatorItem> {

    val result: MutableList<IncubatorItem> = ArrayList(TOPICS_PER_PAGE)

    with(from) {
      check(headers.size == contents.size) { "Headers size should match contents size" }
      check(contents.size == bottoms.size) { "Contents size should match bottoms size" }

      headers.forEachIndexed { index, _ ->
        result.add(
          IncubatorItem(
            title = headers[index].title,
            link = headers[index].link,
            rating = headers[index].rating.toInt(),
            description = contents[index].description,
            images = contents[index].images,
            videos = contents[index].videos,
            videosRaw = contents[index].videosRaw,
            author = bottoms[index].author,
            authorLink = bottoms[index].authorLink,
            date = bottoms[index].date,
            forumName = bottoms[index].forumName,
            forumLink = bottoms[index].forumLink,
            comments = bottoms[index].comments.toInt(),
            cleanedDescription = cleanDescription(contents[index].description)
          )
        )
      }
    }

    return result
  }

  private fun cleanDescription(description: String) =

    with(Jsoup.clean(description, Whitelist().addTags("i", "u", "b", "br"))) {
      if (this.contains("<br>"))
        this.substring(0, this.indexOf("<br>"))
      else
        this
    }
}
