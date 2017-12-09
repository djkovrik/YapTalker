package com.sedsoftware.yaptalker.presentation.executor

import com.sedsoftware.yaptalker.domain.executor.ExecutionThread
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class IOThread : ExecutionThread {

  override fun getScheduler(): Scheduler = Schedulers.io()
}
