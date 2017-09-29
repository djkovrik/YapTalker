package com.sedsoftware.yaptalker.commons

import com.sedsoftware.yaptalker.data.remote.video.VideoTypes
import com.sedsoftware.yaptalker.data.remote.video.parseLink
import com.winterbe.expekt.should
import org.junit.Test

class VideoLinkParserKtTest {

  companion object {
    val coubLink = "//coub.com/embed/vtt0g"
    val coubId = "vtt0g"
    val youtubeLink = "//www.youtube.com/embed/gnUmyxjyHao?wmode=transparent"
    val youtubeId = "gnUmyxjyHao"
    val rutubeLink = "//rutube.ru/video/embed/5de38e8f0e4921075e18cd5cc6f492dd"
    val rutubeId = "5de38e8f0e4921075e18cd5cc6f492dd"
    val yaplakalLink = "//www.yapfiles.ru/get_player/?v=vMDE3MzcwNjEt835d"
    val yaplakalId = "vMDE3MzcwNjEt835d"
    val vkVideoLink = "https://vk.com/video_ext.php?oid=-52925039&id=456241155&hash=5e0fd5c0ce1d2723"
    val vkVideoId = "-52925039_456241155"
  }

  @Test
  fun test_parseLinkReturnsCorrectId() {
    VideoTypes.COUB.should.equal(parseLink(coubLink).first)
    coubId.should.equal(parseLink(coubLink).second)

    VideoTypes.YOUTUBE.should.equal(
        parseLink(youtubeLink).first)
    youtubeId.should.equal(parseLink(youtubeLink).second)

    VideoTypes.RUTUBE.should.equal(
        parseLink(rutubeLink).first)
    rutubeId.should.equal(parseLink(rutubeLink).second)

    VideoTypes.YAP_FILES.should.equal(
        parseLink(yaplakalLink).first)
    yaplakalId.should.equal(parseLink(yaplakalLink).second)

    VideoTypes.VK.should.equal(parseLink(vkVideoLink).first)
    vkVideoId.should.equal(parseLink(vkVideoLink).second)
  }
}