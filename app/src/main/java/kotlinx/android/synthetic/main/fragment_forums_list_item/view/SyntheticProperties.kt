package kotlinx.android.synthetic.main.fragment_forums_list_item.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

val View.forum_image: ImageView
    get() = syntheticView(R.id.forum_image)

val View.linearLayoutMain: LinearLayout
    get() = syntheticView(R.id.linearLayoutMain)

val View.forum_title: TextView
    get() = syntheticView(R.id.forum_title)

val View.forum_last_topic_title: TextView
    get() = syntheticView(R.id.forum_last_topic_title)

val View.linearLayoutRight: LinearLayout
    get() = syntheticView(R.id.linearLayoutRight)

val View.forum_last_topic_author: TextView
    get() = syntheticView(R.id.forum_last_topic_author)

val View.forum_last_topic_date: TextView
    get() = syntheticView(R.id.forum_last_topic_date)
