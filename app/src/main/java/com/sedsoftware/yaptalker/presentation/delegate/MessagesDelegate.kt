package com.sedsoftware.yaptalker.presentation.delegate

import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.colorFromAttr
import java.lang.ref.WeakReference

class MessagesDelegate(private val activity: WeakReference<AppCompatActivity>) {

    fun showMessageError(message: String) {
        showTopSnackbar(message, R.attr.snackColorError, R.attr.snackColorText)
    }

    fun showMessageSuccess(message: String) {
        showTopSnackbar(message, R.attr.snackColorSuccess, R.attr.snackColorText)
    }

    fun showMessageInfo(message: String) {
        showTopSnackbar(message, R.attr.snackColorInfo, R.attr.snackColorText)
    }

    fun showMessageWarning(message: String) {
        showTopSnackbar(message, R.attr.snackColorWarning, R.attr.snackColorText)
    }

    private fun showTopSnackbar(message: String, @AttrRes bgColor: Int, @AttrRes textColor: Int) {
        activity.get()?.let { appCompatActivity ->
            Snackbar
                .make(appCompatActivity.findViewById(R.id.content_container), message, Snackbar.LENGTH_SHORT)
                .also { it.view.setBackgroundColor(appCompatActivity.colorFromAttr(bgColor)) }
                .also {
                    (it.view.findViewById(com.google.android.material.R.id.snackbar_text) as? TextView)
                        ?.setTextColor(appCompatActivity.colorFromAttr(textColor))
                }
                .show()
        }
    }
}
