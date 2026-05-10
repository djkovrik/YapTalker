package kotlinx.android.synthetic.main.activity_topic_gallery_item.view

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView

val View.gallery_image: PhotoView
    get() = syntheticView(R.id.gallery_image)

val View.gallery_image_progress: ProgressBar
    get() = syntheticView(R.id.gallery_image_progress)

val View.gallery_image_progress_label: TextView
    get() = syntheticView(R.id.gallery_image_progress_label)
