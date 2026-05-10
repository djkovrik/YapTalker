package kotlinx.android.synthetic.main.activity_topic_gallery_item_load_more.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView

val View.load_more_button: FrameLayout
    get() = syntheticView(R.id.load_more_button)

val View.load_more_label: TextView
    get() = syntheticView(R.id.load_more_label)

val View.load_more_progress: ProgressBar
    get() = syntheticView(R.id.load_more_progress)
