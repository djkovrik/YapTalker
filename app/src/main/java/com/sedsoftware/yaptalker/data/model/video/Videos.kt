package com.sedsoftware.yaptalker.data.model.video

data class RutubeData(val thumbnail_url: String)
data class CoubData(val thumbnail_url: String)
data class YapVideoData(val player: YapVideoPlayer)
data class YapVideoPlayer(val poster: String)
