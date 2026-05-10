package kotlinx.android.synthetic.main.fragment_chosen_topic_item.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayout

val View.post_author_avatar: ImageView
    get() = syntheticView(R.id.post_author_avatar)

val View.post_author: TextView
    get() = syntheticView(R.id.post_author)

val View.topic_starter_icon: ImageView
    get() = syntheticView(R.id.topic_starter_icon)

val View.post_date: TextView
    get() = syntheticView(R.id.post_date)

val View.post_content: LinearLayout
    get() = syntheticView(R.id.post_content)

val View.post_content_text_container: LinearLayout
    get() = syntheticView(R.id.post_content_text_container)

val View.post_content_image_container: LinearLayout
    get() = syntheticView(R.id.post_content_image_container)

val View.post_content_video_container: LinearLayout
    get() = syntheticView(R.id.post_content_video_container)

val View.post_content_tags_container: FlexboxLayout
    get() = syntheticView(R.id.post_content_tags_container)

val View.post_button_reply: TextView
    get() = syntheticView(R.id.post_button_reply)

val View.post_button_edit: TextView
    get() = syntheticView(R.id.post_button_edit)

val View.post_rating_block: LinearLayout
    get() = syntheticView(R.id.post_rating_block)

val View.post_rating_thumb_down_available: TextView
    get() = syntheticView(R.id.post_rating_thumb_down_available)

val View.post_rating_thumb_down: TextView
    get() = syntheticView(R.id.post_rating_thumb_down)

val View.post_rating: TextView
    get() = syntheticView(R.id.post_rating)

val View.post_rating_thumb_up: TextView
    get() = syntheticView(R.id.post_rating_thumb_up)

val View.post_rating_thumb_up_available: TextView
    get() = syntheticView(R.id.post_rating_thumb_up_available)
