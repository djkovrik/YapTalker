package com.sedsoftware.yaptalker.presentation.commons.extensions

fun String.validateUrl(): String =
    when {
      startsWith("http") -> this
      else -> "http:$this"
    }
