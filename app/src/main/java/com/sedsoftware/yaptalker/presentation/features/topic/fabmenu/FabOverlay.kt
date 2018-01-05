package com.sedsoftware.yaptalker.presentation.features.topic.fabmenu

import android.content.Context
import android.view.View
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.showView

class FabOverlay(
    private val context: Context?,
    private val view: View
) : FabMenuItem {

  override fun show() {
    view.showView()
  }

  override fun hide() {
    view.hideView()
  }
}
