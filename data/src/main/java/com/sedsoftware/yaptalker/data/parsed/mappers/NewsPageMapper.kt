package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.data.parsed.NewsPageParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import java.util.ArrayList
import javax.inject.Inject

/**
 * Mapper class used to transform parsed news page from the data layer into BaseEntity list in the domain layer.
 */
class NewsPageMapper @Inject constructor() {

  companion object {
    private const val NEWS_PER_PAGE = 50
  }

  fun transform(newsPage: NewsPageParsed): List<BaseEntity> {

    val result: MutableList<BaseEntity> = ArrayList(NEWS_PER_PAGE)

    with(newsPage) {
      check(headers.size == contents.size) { "Headers size should match contents size" }
      check(contents.size == bottoms.size) { "Contents size should match bottoms size" }

      headers.forEachIndexed { index, _ ->
        result.add(NewsItem(
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
        ))
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
