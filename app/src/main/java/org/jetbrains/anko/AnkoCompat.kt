package org.jetbrains.anko

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

fun bundleOf(vararg pairs: Pair<String, Any?>): Bundle =
    androidx.core.os.bundleOf(*pairs)

fun Context.browse(url: String, newTask: Boolean = false): Boolean =
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        true
    } catch (_: ActivityNotFoundException) {
        false
    }

fun Context.share(text: String, subject: String = ""): Boolean =
    try {
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, text)
            .putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(Intent.createChooser(intent, subject))
        true
    } catch (_: ActivityNotFoundException) {
        false
    }
