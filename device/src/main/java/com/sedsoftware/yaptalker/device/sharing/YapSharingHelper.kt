package com.sedsoftware.yaptalker.device.sharing

import android.content.Context
import com.sedsoftware.yaptalker.domain.device.SharingHelper
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import javax.inject.Inject

class YapSharingHelper @Inject constructor(
    private val context: Context
) : SharingHelper {

    override fun shareImage(url: String): Completable =
        Completable.fromAction {
            if (url.isNotEmpty()) {
                Picasso
                    .with(context)
                    .load(url)
                    .into(ShareTarget(context))
            }
        }
}
