package com.igweze.ebi.koinmvvm.di

import com.igweze.ebi.koinmvvm.BuildConfig
import com.igweze.ebi.koinmvvm.data.managers.ContactManager
import com.igweze.ebi.koinmvvm.data.managers.ContactServerManager
import com.igweze.ebi.koinmvvm.data.managers.SharedPreferenceManager
import com.igweze.ebi.koinmvvm.data.storage.AppDatabase
import com.igweze.ebi.koinmvvm.log.ProductionTree
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import org.koin.experimental.builder.single
import timber.log.Timber


private val appModule = module {

    single {
        if (BuildConfig.DEBUG) Timber.DebugTree()
        else ProductionTree()
    }

}

private val dataModule = module {

    single { AppDatabase.getInstance(androidContext()) }

    single { get<AppDatabase>().contactDao }

    single<SharedPreferenceManager>()

    single<ContactManager>()

    single<ContactServerManager>()
}


val appModules = listOf(appModule, dataModule)