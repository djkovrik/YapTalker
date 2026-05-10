package kotlinx.android.synthetic.main.fragment_site_search_topic_item.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView

val View.relativeLayoutTopic: RelativeLayout
    get() = syntheticView(R.id.relativeLayoutTopic)

val View.search_topic_name: IconicsTextView
    get() = syntheticView(R.id.search_topic_name)

val View.search_topic_forum: TextView
    get() = syntheticView(R.id.search_topic_forum)

val View.search_topic_last_post_date: TextView
    get() = syntheticView(R.id.search_topic_last_post_date)

val View.search_topic_answers: TextView
    get() = syntheticView(R.id.search_topic_answers)

val View.search_topic_rating: TextView
    get() = syntheticView(R.id.search_topic_rating)
