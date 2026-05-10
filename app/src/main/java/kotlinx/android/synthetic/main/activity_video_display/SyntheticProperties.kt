package kotlinx.android.synthetic.main.activity_video_display

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebView
import android.widget.RelativeLayout

val Activity.content_container: RelativeLayout
    get() = syntheticView(R.id.content_container)

val Fragment.content_container: RelativeLayout
    get() = syntheticView(R.id.content_container)

val View.content_container: RelativeLayout
    get() = syntheticView(R.id.content_container)

val Activity.non_video_layout: RelativeLayout
    get() = syntheticView(R.id.non_video_layout)

val Fragment.non_video_layout: RelativeLayout
    get() = syntheticView(R.id.non_video_layout)

val View.non_video_layout: RelativeLayout
    get() = syntheticView(R.id.non_video_layout)

val Activity.video_view: WebView
    get() = syntheticView(R.id.video_view)

val Fragment.video_view: WebView
    get() = syntheticView(R.id.video_view)

val View.video_view: WebView
    get() = syntheticView(R.id.video_view)

val Activity.video_layout: RelativeLayout
    get() = syntheticView(R.id.video_layout)

val Fragment.video_layout: RelativeLayout
    get() = syntheticView(R.id.video_layout)

val View.video_layout: RelativeLayout
    get() = syntheticView(R.id.video_layout)

val Activity.video_loading: View
    get() = syntheticView(R.id.video_loading)

val Fragment.video_loading: View
    get() = syntheticView(R.id.video_loading)

val View.video_loading: View
    get() = syntheticView(R.id.video_loading)
