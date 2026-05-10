package kotlinx.android.synthetic.main.fragment_active_topics_list_item.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.sedsoftware.yaptalker.presentation.custom.view.EllipsizingTextView

val View.active_topic_name: EllipsizingTextView
    get() = syntheticView(R.id.active_topic_name)

val View.active_topic_answers: IconicsTextView
    get() = syntheticView(R.id.active_topic_answers)

val View.active_topic_last_post_date: TextView
    get() = syntheticView(R.id.active_topic_last_post_date)

val View.active_topic_forum: TextView
    get() = syntheticView(R.id.active_topic_forum)

val View.active_topic_rating: TextView
    get() = syntheticView(R.id.active_topic_rating)
