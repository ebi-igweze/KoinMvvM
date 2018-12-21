package com.igweze.ebi.koinmvvm

import android.app.Application
import android.content.Context
import com.igweze.ebi.koinmvvm.di.activityModules
import com.igweze.ebi.koinmvvm.di.appModules
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.test.KoinTest
import org.koin.test.checkModules
import org.mockito.Mockito.mock


class DependencyGraphTest: KoinTest {

    @Test
    fun checkDependencyGraph() {

        // override any previously defined dependencies
        val mockApplication = module(override = true) {

            single { mock(Application::class.java) }
            single { mock(Context::class.java) }
        }


        // get all modules
        val moduleList = appModules + activityModules + mockApplication

        checkModules(moduleList)
    }
}