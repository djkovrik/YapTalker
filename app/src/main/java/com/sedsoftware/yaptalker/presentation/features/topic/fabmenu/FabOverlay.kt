package com.sedsoftware.yaptalker.presentation.features.topic.fabmenu

import android.view.View
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.showView

class FabOverlay(private val view: View) : FabMenuItem {

  override fun show() {
    view.showView()
  }

  override fun hide() {
    view.hideView()
  }
}
