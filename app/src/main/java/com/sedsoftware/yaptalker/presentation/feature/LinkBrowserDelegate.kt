package com.sedsoftware.yaptalker.presentation.feature

import android.content.Context
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import org.jetbrains.anko.browse
import javax.inject.Inject

class LinkBrowserDelegate @Inject constructor(private val context: Context?) {

    fun browse(link: String) {
        context?.browse(link.validateUrl())
    }
}
