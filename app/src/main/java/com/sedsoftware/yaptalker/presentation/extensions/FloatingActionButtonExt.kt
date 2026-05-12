package com.sedsoftware.yaptalker.presentation.extensions

import android.content.res.ColorStateList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.sedsoftware.yaptalker.R

fun FloatingActionButton.setIconicsImage(icon: IIcon, paddingRes: Int = R.dimen.fab_icon_padding) {
    imageTintList = null as ColorStateList?
    setImageDrawable(
        IconicsDrawable(context)
            .icon(icon)
            .color(context.colorFromAttr(R.attr.colorFabIcon))
            .sizeDp(24)
            .paddingRes(paddingRes)
    )
}
