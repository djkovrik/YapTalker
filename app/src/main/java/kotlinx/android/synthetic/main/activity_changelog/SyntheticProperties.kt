package kotlinx.android.synthetic.main.activity_changelog

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView

val Activity.changelog_container: ScrollView
    get() = syntheticView(R.id.changelog_container)

val Fragment.changelog_container: ScrollView
    get() = syntheticView(R.id.changelog_container)

val View.changelog_container: ScrollView
    get() = syntheticView(R.id.changelog_container)

val Activity.changelog: TextView
    get() = syntheticView(R.id.changelog)

val Fragment.changelog: TextView
    get() = syntheticView(R.id.changelog)

val View.changelog: TextView
    get() = syntheticView(R.id.changelog)

val Activity.changelog_progressbar: ProgressBar
    get() = syntheticView(R.id.changelog_progressbar)

val Fragment.changelog_progressbar: ProgressBar
    get() = syntheticView(R.id.changelog_progressbar)

val View.changelog_progressbar: ProgressBar
    get() = syntheticView(R.id.changelog_progressbar)
