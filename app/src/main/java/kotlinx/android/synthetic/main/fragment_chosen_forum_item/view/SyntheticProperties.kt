package kotlinx.android.synthetic.main.fragment_chosen_forum_item.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.sedsoftware.yaptalker.presentation.custom.view.EllipsizingTextView

val View.topic_name: EllipsizingTextView
    get() = syntheticView(R.id.topic_name)

val View.topic_answers: IconicsTextView
    get() = syntheticView(R.id.topic_answers)

val View.topic_last_post_date: TextView
    get() = syntheticView(R.id.topic_last_post_date)

val View.topic_last_post_author: TextView
    get() = syntheticView(R.id.topic_last_post_author)

val View.topic_rating: TextView
    get() = syntheticView(R.id.topic_rating)
