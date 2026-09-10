package com.sedsoftware.yaptalker.data.mapper

import com.google.gson.Gson
import com.sedsoftware.yaptalker.data.network.site.model.SettingsResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class UserProfileMapperTest {

    private val mapper = UserProfileMapper()

    @Test
    fun `maps profile response used by the reference client`() {
        val response = Gson().fromJson(PROFILE_RESPONSE, SettingsResult::class.java)
        val apiProfile = response.profile?.firstOrNull()

        assertNotNull(apiProfile)
        val profile = mapper.apply(requireNotNull(apiProfile))

        assertEquals("Test user", profile.nickname)
        assertEquals("Users", profile.group)
        assertEquals(42, profile.uq)
        assertEquals("123", profile.messagesCount)
        assertEquals("31.12.2020", profile.registerDate)
        assertEquals("01.01.1990", profile.birthDate)
        assertEquals("m", profile.sex)
        assertEquals("Hello", profile.signature)
    }

    @Test
    fun `uses safe defaults for optional API fields`() {
        val response = Gson().fromJson("{\"profile\":[{}]}", SettingsResult::class.java)
        val profile = mapper.apply(requireNotNull(response.profile?.firstOrNull()))

        assertEquals("", profile.nickname)
        assertEquals("", profile.registerDate)
        assertEquals(0, profile.uq)
    }

    private companion object {
        val PROFILE_RESPONSE = """
            {
              "code": 0,
              "profile": [{
                "id": "151247",
                "name": "Test user",
                "group_title": "Users",
                "avatar_url": "https://example.com/avatar.jpg",
                "photo_url": "https://example.com/photo.jpg",
                "joined": "2020-12-31 10:20:30",
                "posts": "123",
                "rank_value": "42",
                "location": "Moscow",
                "sex": "m",
                "birthday": "1990-01-01",
                "status": "Hello",
                "email": "user@example.com",
                "time_zone": "+03:00"
              }]
            }
        """.trimIndent()
    }
}
