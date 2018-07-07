package com.sedsoftware.yaptalker.presentation.feature.gallery

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel

@StateStrategyType(SkipStrategy::class)
interface TopicGalleryView : MvpView, CanShowErrorMessage {

    @StateStrategyType(AddToEndStrategy::class)
    fun appendImages(images: List<DisplayedItemModel>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateCurrentUiState(title: String)

    fun scrollToFirstNewImage(newImagesOffset: Int)

    fun scrollToSelectedImage(imageUrl: String)

    fun lastPageReached()

    fun fileSavedMessage(filepath: String)

    fun fileNotSavedMessage()
}
