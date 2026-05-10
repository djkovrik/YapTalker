package kotlinx.android.synthetic.main.activity_image_display

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView

val Activity.content_container: FrameLayout
    get() = syntheticView(R.id.content_container)

val Fragment.content_container: FrameLayout
    get() = syntheticView(R.id.content_container)

val View.content_container: FrameLayout
    get() = syntheticView(R.id.content_container)

val Activity.photo_view: PhotoView
    get() = syntheticView(R.id.photo_view)

val Fragment.photo_view: PhotoView
    get() = syntheticView(R.id.photo_view)

val View.photo_view: PhotoView
    get() = syntheticView(R.id.photo_view)

val Activity.image_progress: ProgressBar
    get() = syntheticView(R.id.image_progress)

val Fragment.image_progress: ProgressBar
    get() = syntheticView(R.id.image_progress)

val View.image_progress: ProgressBar
    get() = syntheticView(R.id.image_progress)

val Activity.image_progress_label: TextView
    get() = syntheticView(R.id.image_progress_label)

val Fragment.image_progress_label: TextView
    get() = syntheticView(R.id.image_progress_label)

val View.image_progress_label: TextView
    get() = syntheticView(R.id.image_progress_label)
