package com.sedsoftware.yaptalker.data.thumbnail

class RutubeData(val thumbnail_url: String)

class CoubData(val thumbnail_url: String)

class YapVideoData(val player: YapVideoPlayer)

class YapVideoPlayer(val poster: String)

class VkResponseWrapper(val response: VkResponse)

class VkResponse(val count: Int, val items: List<VkVideoData>)

class VkVideoData(val photo_320: String)
