package com.sedsoftware.yaptalker.presentation.model.base

data class SinglePostParsedModel(
    val content: MutableList<PostContentModel>,
    val images: MutableList<String>,
    var videos: List<String>,
    var videosRaw: List<String>,
    var videosLinks: List<String>,
    var videoTypes: List<String>
)
