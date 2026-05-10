package com.sedsoftware.yaptalker.synthetic

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment

internal fun <T : View> Activity.syntheticView(id: Int): T = findViewById(id)

internal fun <T : View> Fragment.syntheticView(id: Int): T = requireView().findViewById(id)

internal fun <T : View> View.syntheticView(id: Int): T = findViewById(id)
