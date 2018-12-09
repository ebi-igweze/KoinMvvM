package com.igweze.ebi.koinmvvm

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.disposables.Disposable
import io.reactivex.Scheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * This class makes sure that Android schedulers are present for
 * unit testing, and makes sure they run synchronously, making it simpler to test.
 * Source [Medium](https://medium.com/@peter.tackage/overriding-rxandroid-schedulers-in-rxjava-2-5561b3d14212)
 * */

class RxSchedulerRule : TestRule {
    private val immediate = object : Scheduler() {
        override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            // this prevents StackOverflowErrors when scheduling with a delay
            return super.scheduleDirect(run, 0, unit)
        }

        override fun createWorker(): Scheduler.Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
        }
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { immediate }
                RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
                RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
                RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}