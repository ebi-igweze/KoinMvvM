package com.igweze.ebi.koinmvvm.di

import com.igweze.ebi.koinmvvm.data.managers.ContactManager
import com.igweze.ebi.koinmvvm.data.storage.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module


private val dataModule = module {

    single { AppDatabase.getInstance(androidContext()) }

    single { get<AppDatabase>().contactDao }

    single { ContactManager(get()) }
}


val appModules = listOf(dataModule)