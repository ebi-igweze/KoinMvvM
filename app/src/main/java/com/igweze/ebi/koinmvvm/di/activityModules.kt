package com.igweze.ebi.koinmvvm.di

import android.arch.lifecycle.LifecycleOwner
import com.igweze.ebi.koinmvvm.di.ModuleConstants.DETAIL_SCOPE
import com.igweze.ebi.koinmvvm.services.SyncObserver
import com.igweze.ebi.koinmvvm.viewmodels.DetailViewModel
import com.igweze.ebi.koinmvvm.viewmodels.MainViewModel
import com.igweze.ebi.koinmvvm.viewmodels.RemoteContactViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val syncModule = module {

    factory { (lifecycleOwner: LifecycleOwner) ->  SyncObserver(lifecycleOwner , get(), get()) }

}

val mainModule = module {

    viewModel { MainViewModel(get()) }
}

val remoteModule = module {

    viewModel { RemoteContactViewModel(get()) }
}

val detailsModule = module {

    scope(DETAIL_SCOPE) { DetailViewModel(get()) }

}


val activitiesModules = listOf(mainModule, detailsModule, remoteModule, syncModule)

object ModuleConstants {

    const val DETAIL_SCOPE = "com.igweze.ebi.details_scope"
}