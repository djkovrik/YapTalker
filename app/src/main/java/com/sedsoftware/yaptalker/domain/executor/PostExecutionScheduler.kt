package com.sedsoftware.yaptalker.domain.executor

import io.reactivex.Scheduler

interface PostExecutionScheduler {
  fun getScheduler(): Scheduler
}
