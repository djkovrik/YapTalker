package com.sedsoftware.domain.entity

/**
 * Class which contains single post parsed content.
 */
class SinglePostParsed(
    val content: MutableList<PostContent>,
    val images: MutableList<String>,
    val videos: MutableList<String>,
    val videosRaw: MutableList<String>
)
