package com.sedsoftware.yaptalker.presentation.extensions

fun String.validateUrl(): String =
    when {
      startsWith("http") -> this
      else -> "http:$this"
    }
