package com.igweze.ebi.koinmvvm

import android.app.Application
import com.igweze.ebi.koinmvvm.di.activitiesModules
import com.igweze.ebi.koinmvvm.di.appModules
import org.koin.android.ext.android.startKoin

class AddressBookApp: Application() {


    override fun onCreate() {
        super.onCreate()

        val moduleList = appModules + activitiesModules

        startKoin(this, moduleList)
    }
}