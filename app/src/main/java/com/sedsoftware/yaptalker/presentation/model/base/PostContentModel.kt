package com.sedsoftware.yaptalker.presentation.model.base

import android.text.Spanned

sealed class PostContentModel {
    class PostTextModel(val text: String) : PostContentModel()
    class PostQuoteModel(val text: String) : PostContentModel()
    class PostQuoteAuthorModel(val text: Spanned) : PostContentModel()
    class PostHiddenTextModel(val text: String) : PostContentModel()
    class PostScriptModel(val text: Spanned) : PostContentModel()
    class PostWarningModel(val text: Spanned) : PostContentModel()
}
