package com.sedsoftware.yaptalker.data.thumbnail

data class RutubeData(val thumbnail_url: String)

data class CoubData(val thumbnail_url: String)

data class YapVideoData(val player: YapVideoPlayer)

data class YapVideoPlayer(val poster: String)

data class VkResponseWrapper(val response: VkResponse)

data class VkResponse(val count: Int, val items: List<VkVideoData>)

data class VkVideoData(val photo_320: String)
