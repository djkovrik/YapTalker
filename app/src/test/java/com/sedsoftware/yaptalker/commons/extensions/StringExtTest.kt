package com.sedsoftware.yaptalker.commons.extensions

import com.winterbe.expekt.should
import org.junit.Test

class StringExtTest {

  @Test
  fun chopEdgesReturnsCorrectString() {
    // Arrange
    val commentsString = "(12345)"
    val commentsStringShortened = "12345"
    val randomString = "as#Dw56DF85DFh5DF"
    val randomStringShortened = "s#Dw56DF85DFh5D"

    // Assert
    commentsStringShortened.should.equal(commentsString.chopEdges())
    randomStringShortened.should.equal(randomString.chopEdges())
  }

  @Test
  fun getLastDigitsReturnsCorrectInt() {
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
  fun toMd5ReturnsCorrectString() {
    // Arrange
    val testMap = mapOf(
        "Admin1506268417166" to "a9878e9d3f77aeff9825808ab6b620b6",
        "Admin1506268417167" to "2c741da712b127c76fe45527984dd203",
        "Admin2708268217521" to "1cb1741da29300f33779eaa48a008f6c",
        "User1506268417166" to "af069489bbe914c40b7b12e92327c374",
        "User1506268417167" to "e968260d46e1a0a2dd0de853f262c2e9",
        "Longnickname1506268417167" to "50d6b85ba9de8af82c9b32733544a06e",
        "Longnickname" to "2197d86978dc3664a15382698a1795b4",
        "Admin" to "e3afed0047b08059d0fada10f400c1e5",
        "User" to "8f9bfe9d1345237cb3b2b205864da075")

    // Assert
    testMap.forEach { string, hash ->
      string.toMd5().should.equal(hash)
    }
  }

  @Test
  fun validateUrlReturnsCorrectString() {
    // Arrange
    val rawUrl1 = "http://www.yaplakal.com/html/static/top-logo.png"
    val rawUrl2 = "//www.yaplakal.com/html/static/top-logo.png"
    val validatedUrl = "http://www.yaplakal.com/html/static/top-logo.png"

    // Assert
    rawUrl1.validateUrl().should.equal(validatedUrl)
    rawUrl2.validateUrl().should.equal(validatedUrl)
  }

  @Test
  fun extractYapIdsReturnsCorrectTriple() {

    // Triple format:
    // ForumId - TopicId - startingPostNumber

    // Arrange
    val testMap = mapOf(
        "http://www.yaplakal.com/forum1/topic1672650.html" to Triple(1, 1672650, 0),
        "http://www.yaplakal.com/forum1/st/25/topic1687325.html" to Triple(1, 1687325, 25),
        "http://www.yaplakal.com/forum24/st/375/topic690940.html" to Triple(24, 690940, 375),
        "http://www.yaplakal.com/forum28/st/0/topic1687465.html" to Triple(28, 1687465, 0),
        "https://www.google.ru" to Triple(0, 0, 0)
    )

    // Assert
    testMap.forEach { url, triple ->
      url.extractYapIds().should.equal(triple)
    }
  }
}
