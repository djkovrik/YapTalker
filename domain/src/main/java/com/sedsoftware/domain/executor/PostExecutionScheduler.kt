package com.sedsoftware.domain.executor

import io.reactivex.Scheduler

interface PostExecutionScheduler {
  fun getScheduler(): Scheduler
}
