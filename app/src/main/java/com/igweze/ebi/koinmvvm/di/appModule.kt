package com.igweze.ebi.koinmvvm.di

import com.igweze.ebi.koinmvvm.data.managers.ContactManager
import com.igweze.ebi.koinmvvm.data.managers.ContactServerManager
import com.igweze.ebi.koinmvvm.data.managers.SharedPreferenceManager
import com.igweze.ebi.koinmvvm.data.storage.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module


private val dataModule = module {

    single { AppDatabase.getInstance(androidContext()) }

    single { get<AppDatabase>().contactDao }

    single { SharedPreferenceManager(androidContext()) }

    single { ContactManager(get()) }

    single { ContactServerManager(get())}
}


val appModules = listOf(dataModule)