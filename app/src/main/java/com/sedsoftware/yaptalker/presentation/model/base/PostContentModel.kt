package com.sedsoftware.yaptalker.presentation.model.base

/**
 * Class contains all possible content types for SinglePost.
 */
sealed class PostContentModel {
  class PostTextModel(val text: String) : PostContentModel()
  class PostQuoteModel(val text: String) : PostContentModel()
  class PostQuoteAuthorModel(val text: String) : PostContentModel()
  class PostHiddenTextModel(val text: String) : PostContentModel()
  class PostScriptModel(val text: String) : PostContentModel()
  class PostWarningModel(val text: String) : PostContentModel()
}
