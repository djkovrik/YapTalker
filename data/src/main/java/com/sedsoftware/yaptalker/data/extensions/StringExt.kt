package com.sedsoftware.yaptalker.data.extensions

import java.util.regex.Pattern

fun String.getLastDigits(): Int {

    val regex = Pattern.compile("(\\d+)(?!.*\\d)")
    val matcher = regex.matcher(this)

    if (matcher.find()) {
        return try {
            Integer.parseInt(matcher.group(1))
        } catch (e: NumberFormatException) {
            0
        }
    }

    return 0
}
