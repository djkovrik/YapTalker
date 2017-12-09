package com.sedsoftware.yaptalker.presentation.executor

import com.sedsoftware.yaptalker.domain.executor.PostExecutionThread
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

class UIThread : PostExecutionThread {

  override fun getScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
