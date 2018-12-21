package com.igweze.ebi.koinmvvm

import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.error.NoScopeException
import org.koin.experimental.builder.single
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.get
import org.koin.standalone.getKoin
import org.koin.test.KoinTest
import org.koin.test.ext.koin.allInstances

class SampleService()
class SampleSecondService()
class SampleThirdService()

class KoinTests : KoinTest {


    @After
    fun closeKoin() {
       stopKoin()
    }

    @Test
    fun `should resolve dependencies with moudle paths`() {

        // declare module and sub-modules
        val mockModule = module(path = "root") {
            single { SampleService() }

            module(path = "staging") {
                single { SampleSecondService() }

                module(path = "test") {
                    single { SampleThirdService() }
                }
            }

            module(path = "production") {

                single { SampleSecondService() }

            }
        }


        // start koin
        startKoin(listOf(mockModule))


        // retrieve dependencies
        val secondService: SampleSecondService =
                get("root.staging.SampleSecondService")

        val thirdTestService: SampleThirdService =
                get("root.staging.test.SampleThirdService")


        assertNotNull(secondService)
        assertNotNull(thirdTestService)
    }

    @Test
    fun `should resolve dependency with name attribute`() {
        // declare module and sub-modules
        val mockModule = module {
            single(name = "service_name") { SampleService() }
        }

        startKoin(listOf(mockModule))

        val service: SampleService = get("service_name")

        assertNotNull(service)
    }

    @Test
    fun `should immediately create only the dependency marked with 'createOnStart = true', on koinStart`() {

        val mockModule = module {
            single<SampleService>(createOnStart = true)
            single<SampleSecondService>()
        }


        startKoin(listOf(mockModule))

        val instances = getKoin().allInstances()
        assertTrue("Koin instances are more than one: ${instances.size}", instances.size == 1)

        // get service that has bean initialized early
        val holder = instances.firstOrNull { holder -> holder.bean.isEager && holder.bean.clazz == SampleService::class }

        // ensure other service was not initialized
        val holder2 = instances.firstOrNull { holder -> !holder.bean.isEager && holder.bean.clazz == SampleSecondService::class }


        assertNotNull(holder)
        assertNull(holder2)
    }

    @Test
    fun `should immediately create the entire module-dependencies marked with 'createOnStart = true'`() {

        val mockModule = module(createOnStart = true) {
            single<SampleSecondService>()
            single<SampleService>()
        }


        startKoin(listOf(mockModule))

        val instances = getKoin().allInstances()
        assertTrue("Koin instances are more than one: ${instances.size}", instances.size == 2)

        // ensure other service was not initialized
        val holder = instances.firstOrNull { holder -> holder.bean.isEager && holder.bean.clazz == SampleService::class }

        // get service that has bean initialized early
        val holder2 = instances.firstOrNull { h -> h.bean.isEager && h.bean.clazz == SampleSecondService::class }


        assertNotNull(holder2)
        assertNotNull(holder)

    }


    @Test
    fun `should provide overridden dependencies marked with 'override = true'`() {

        val testSample = SampleService()

        val actualModule = module {
            single<SampleService>()
        }

        val testModule = module {
            single(override = true) { testSample }
        }

        startKoin(listOf(actualModule))
        loadKoinModules(testModule)

        val service: SampleService = get()

        assertSame(testSample, service)
    }

    @Test
    fun `should provide overridden module dependencies marked with 'override = true'`() {

        val testSample = SampleService()
        val testSample2 = SampleSecondService()
        val testSample3 = SampleThirdService()

        val actualModule = module {
            single<SampleService>()
            single<SampleSecondService>()
            single<SampleThirdService>()
        }

        val testModule = module(override = true) {
            single { testSample }
            single { testSample2 }
            single { testSample3 }
        }

        startKoin(listOf(actualModule))

        // load overriding module
        loadKoinModules(testModule)

        val actualService: SampleService = get()
        val actualService2: SampleSecondService = get()
        val actualService3: SampleThirdService = get()


        assertSame(testSample, actualService)
        assertSame(testSample2, actualService2)
        assertSame(testSample3, actualService3)
    }

    @Test(expected = NoScopeException::class)
    fun `should throw execption when getting dependdency without creating scope`() {

        val scopeKey = "ActivityScope"

        val actualModule = module {
            scope(scopeKey) { SampleService() }
            scope(scopeKey) { SampleSecondService() }
            scope(scopeKey) { SampleThirdService() }
        }

        startKoin(listOf(actualModule))

        // should throw exception when retrieving this dependency
        val s = get<SampleService>()
    }

    @Test
    fun `should resolve scoped dependencies`() {

        val scopeKey = "ActivityScope"

        val actualModule = module {
            scope(scopeKey) { SampleService() }
            scope(scopeKey) { SampleSecondService() }
            scope(scopeKey) { SampleThirdService() }
        }

        startKoin(listOf(actualModule))

        getKoin().createScope(scopeKey)

        val s = get<SampleService>()
        assertNotNull(s)
    }

}