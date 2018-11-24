package com.sedsoftware.yaptalker.presentation.feature

import android.content.Context
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import org.jetbrains.anko.browse
import javax.inject.Inject

class LinkBrowserDelegate @Inject constructor(context: Context?) {

    private val weakContext: Context? by weak(context)

    fun browse(link: String) {
        weakContext?.browse(link.validateUrl())
    }
}
