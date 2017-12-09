package com.sedsoftware.yaptalker.commons.extensions

fun String.validateUrl(): String =
    when {
      startsWith("http") -> this
      else -> "http:$this"
    }
