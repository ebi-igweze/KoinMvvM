package com.igweze.ebi.koinmvvm.di

import android.arch.lifecycle.LifecycleOwner
import com.igweze.ebi.koinmvvm.di.ModuleConstants.DETAIL_SCOPE
import com.igweze.ebi.koinmvvm.services.SyncObserver
import com.igweze.ebi.koinmvvm.viewmodels.DetailViewModel
import com.igweze.ebi.koinmvvm.viewmodels.MainViewModel
import com.igweze.ebi.koinmvvm.viewmodels.RemoteContactViewModel
import org.koin.android.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.scope

val syncModule = module {

    factory { (lifecycleOwner: LifecycleOwner) ->  SyncObserver(lifecycleOwner , get(), get()) }

}

val mainModule = module {

    viewModel<MainViewModel>()
}

val remoteModule = module {

    viewModel<RemoteContactViewModel>()
}

val detailsModule = module {
    scope<DetailViewModel>(DETAIL_SCOPE)
}


val activityModules = listOf(mainModule, detailsModule, remoteModule, syncModule)

object ModuleConstants {

    const val DETAIL_SCOPE = "com.igweze.ebi.details_scope"
}