package com.sedsoftware.yaptalker.presentation.features.gallery

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface TopicGalleryView : BaseView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendImages(images: List<YapEntity>)

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun updateCurrentUiState(title: String)

  fun scrollToFirstNewImage(newImagesOffset: Int)

  fun fileSavedMessage(filepath: String)

  fun fileNotSavedMessage()
}
