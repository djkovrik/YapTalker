package com.sedsoftware.yaptalker.commons.extensions

import com.winterbe.expekt.should
import org.junit.Test

class StringExtTest {

  @Test
  fun testStringGetLastDigitsExt() {
    // Arrange
    val testMap = mapOf(
        7 to "http://www.yaplakal.com/forum7/",
        11 to "http://www.yaplakal.com/forum11/",
        28 to "http://www.yaplakal.com/forum28/",
        18620 to "http://www.yaplakal.com/members/member18620.html",
        35301 to "http://www.yaplakal.com/members/member35301.html",
        291678 to "http://www.yaplakal.com/members/member291678.html",
        625385 to "http://www.yaplakal.com/forum7/topic625385.html",
        1633749 to "http://www.yaplakal.com/forum1/topic1633749.html",
        1634928 to "http://www.yaplakal.com/forum2/topic1634928.html",
        12345678 to "1234ww1www2ww344aww55*sds12345678abracadabra",
        61979992 to "entry61979992")

    // Assert
    testMap.forEach { id, link ->
      id.should.equal(link.getLastDigits())
    }
  }

  @Test
  fun testStringChopEdgesExt() {
    // Arrange
    val commentsString = "(12345)"
    val commentsStringShortened = "12345"
    val randomString = "as#Dw56DF85DFh5DF"
    val randomStringShortened = "s#Dw56DF85DFh5D"

    // Assert
    commentsStringShortened.should.equal(commentsString.chopEdges())
    randomStringShortened.should.equal(randomString.chopEdges())
  }
}