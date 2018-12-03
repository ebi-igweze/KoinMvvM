package com.igweze.ebi.koinmvvm

import android.app.Application
import com.igweze.ebi.koinmvvm.di.activitiesModules
import com.igweze.ebi.koinmvvm.di.appModules
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class AddressBookApp: Application() {


    override fun onCreate() {
        super.onCreate()

        // get all modules
        val moduleList = appModules + activitiesModules
        // set the module list
        startKoin(this, moduleList)

        // get log tree implementation
        val tree: Timber.Tree = get()
        Timber.plant(tree)
    }
}