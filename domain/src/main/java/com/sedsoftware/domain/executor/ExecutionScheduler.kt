package com.sedsoftware.domain.executor

import io.reactivex.Scheduler

interface ExecutionScheduler {
  fun getScheduler(): Scheduler
}
