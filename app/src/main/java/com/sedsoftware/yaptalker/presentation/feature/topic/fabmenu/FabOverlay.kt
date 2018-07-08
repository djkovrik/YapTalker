package com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible

class FabOverlay(private val view: View) : FabMenuItem {

    override fun show() {
        view.isVisible = true
    }

    override fun hide() {
        view.isGone = true
    }
}
