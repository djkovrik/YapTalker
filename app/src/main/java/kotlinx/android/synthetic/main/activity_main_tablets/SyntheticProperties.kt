package kotlinx.android.synthetic.main.activity_main_tablets

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.FrameLayout

val Activity.include_appbar: View
    get() = syntheticView(R.id.include_appbar)

val Fragment.include_appbar: View
    get() = syntheticView(R.id.include_appbar)

val View.include_appbar: View
    get() = syntheticView(R.id.include_appbar)

val Activity.navigation_drawer: FrameLayout
    get() = syntheticView(R.id.navigation_drawer)

val Fragment.navigation_drawer: FrameLayout
    get() = syntheticView(R.id.navigation_drawer)

val View.navigation_drawer: FrameLayout
    get() = syntheticView(R.id.navigation_drawer)
