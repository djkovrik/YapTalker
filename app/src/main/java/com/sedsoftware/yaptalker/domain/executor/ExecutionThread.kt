package com.sedsoftware.yaptalker.domain.executor

import io.reactivex.Scheduler

interface ExecutionThread {

  fun getScheduler(): Scheduler
}
