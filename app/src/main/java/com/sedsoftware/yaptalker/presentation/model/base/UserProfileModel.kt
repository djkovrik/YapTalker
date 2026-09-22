package com.sedsoftware.yaptalker.presentation.model.base

import android.text.Spanned

data class UserProfileModel(
    val nickname: String,
    val avatar: String,
    val photo: String,
    val group: String,
    val uq: Spanned,
    val signature: Spanned,
    val registerDate: String,
    val timeZone: String,
    val birthDate: String,
    val location: String,
    val sex: String,
    val messagesCount: String
)
