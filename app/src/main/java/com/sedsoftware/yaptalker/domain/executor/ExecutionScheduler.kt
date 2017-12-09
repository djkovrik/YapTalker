package com.sedsoftware.yaptalker.domain.executor

import io.reactivex.Scheduler

interface ExecutionScheduler {
  fun getScheduler(): Scheduler
}
