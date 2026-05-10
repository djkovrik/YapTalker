package kotlinx.android.synthetic.main.include_main_appbar_transparent

import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.synthetic.syntheticView
import android.app.Activity
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout

val Activity.appbar: AppBarLayout
    get() = syntheticView(R.id.appbar)

val Fragment.appbar: AppBarLayout
    get() = syntheticView(R.id.appbar)

val View.appbar: AppBarLayout
    get() = syntheticView(R.id.appbar)

val Activity.toolbar: Toolbar
    get() = syntheticView(R.id.toolbar)

val Fragment.toolbar: Toolbar
    get() = syntheticView(R.id.toolbar)

val View.toolbar: Toolbar
    get() = syntheticView(R.id.toolbar)
