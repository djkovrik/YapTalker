package kotlinx.android.synthetic.main.activity_blacklist_item.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

val View.blacklist_delete_icon: ImageView
    get() = syntheticView(R.id.blacklist_delete_icon)

val View.blacklisted_topic_title: TextView
    get() = syntheticView(R.id.blacklisted_topic_title)

val View.blacklisted_topic_date: TextView
    get() = syntheticView(R.id.blacklisted_topic_date)
