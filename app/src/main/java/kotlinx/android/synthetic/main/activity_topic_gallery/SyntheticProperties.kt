package kotlinx.android.synthetic.main.activity_topic_gallery

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView

val Activity.content_container: FrameLayout
    get() = syntheticView(R.id.content_container)

val Fragment.content_container: FrameLayout
    get() = syntheticView(R.id.content_container)

val View.content_container: FrameLayout
    get() = syntheticView(R.id.content_container)

val Activity.topic_gallery: RecyclerView
    get() = syntheticView(R.id.topic_gallery)

val Fragment.topic_gallery: RecyclerView
    get() = syntheticView(R.id.topic_gallery)

val View.topic_gallery: RecyclerView
    get() = syntheticView(R.id.topic_gallery)
