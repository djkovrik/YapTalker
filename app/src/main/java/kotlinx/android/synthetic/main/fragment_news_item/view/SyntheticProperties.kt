package kotlinx.android.synthetic.main.fragment_news_item.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mikepenz.iconics.view.IconicsTextView

val View.news_author: TextView
    get() = syntheticView(R.id.news_author)

val View.news_date: TextView
    get() = syntheticView(R.id.news_date)

val View.news_forum: TextView
    get() = syntheticView(R.id.news_forum)

val View.news_title: TextView
    get() = syntheticView(R.id.news_title)

val View.news_content_text: TextView
    get() = syntheticView(R.id.news_content_text)

val View.news_content_image_container: ConstraintLayout
    get() = syntheticView(R.id.news_content_image_container)

val View.news_content_image: ImageView
    get() = syntheticView(R.id.news_content_image)

val View.news_content_image_overlay: TextView
    get() = syntheticView(R.id.news_content_image_overlay)

val View.news_comments_counter: IconicsTextView
    get() = syntheticView(R.id.news_comments_counter)

val View.news_rating: TextView
    get() = syntheticView(R.id.news_rating)
