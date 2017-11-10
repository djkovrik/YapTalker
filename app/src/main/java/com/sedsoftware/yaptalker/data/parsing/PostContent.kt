package com.sedsoftware.yaptalker.data.parsing

interface Content

class PostText(val text: String) : Content

class PostQuote(val text: String) : Content

class PostQuoteAuthor(val text: String) : Content

class PostHiddenText(val text: String) : Content

class PostScript(val text: String) : Content

class PostWarning(val text: String) : Content
