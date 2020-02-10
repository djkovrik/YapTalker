package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.parsed.NewsPageParsed
import com.sedsoftware.yaptalker.domain.entity.base.NewsBlock
import com.sedsoftware.yaptalker.domain.entity.base.NewsItem
import io.reactivex.functions.Function
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import java.util.ArrayList
import javax.inject.Inject

class NewsPageMapper @Inject constructor() : Function<NewsPageParsed, NewsBlock> {

    companion object {
        private const val NEWS_PER_PAGE = 50
    }

    override fun apply(from: NewsPageParsed): NewsBlock {
        val result: MutableList<NewsItem> = ArrayList(NEWS_PER_PAGE)

        with(from) {
            check(headers.size == contents.size) { "Headers size should match contents size" }
            check(contents.size == bottoms.size) { "Contents size should match bottoms size" }

            headers.forEachIndexed { index, _ ->
                result.add(
                    NewsItem(
                        title = headers[index].title,
                        link = headers[index].link,
                        id = headers[index].link.getLastDigits(),
                        rating = headers[index].rating.toInt(),
                        description = contents[index].description,
                        images = contents[index].images,
                        videos = contents[index].videos,
                        videosRaw = contents[index].videosRaw,
                        videosLinks = contents[index].videosLinks,
                        author = bottoms[index].author,
                        authorLink = bottoms[index].authorLink,
                        date = bottoms[index].date,
                        forumName = bottoms[index].forumName,
                        forumLink = bottoms[index].forumLink,
                        comments = bottoms[index].comments.toInt(),
                        cleanedDescription = cleanDescription(contents[index].description),
                        isYapLink = headers[index].link.contains("yaplakal") && !headers[index].link.contains("/go/")
                    )
                )
            }
        }

        return NewsBlock(items = result, offset = from.offset)
    }

    private fun cleanDescription(description: String) =

        with(Jsoup.clean(description, Whitelist().addTags("i", "u", "b", "br"))) {
            if (this.contains("<br>"))
                this.substring(0, this.indexOf("<br>"))
            else
                this
        }
}
