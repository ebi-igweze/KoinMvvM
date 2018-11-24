package com.igweze.ebi.koinmvvm.di

import com.igweze.ebi.koinmvvm.di.ModuleConstants.DETAIL_SCOPE
import com.igweze.ebi.koinmvvm.viewmodels.DetailViewModel
import com.igweze.ebi.koinmvvm.viewmodels.MainViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val mainModule = module {

    viewModel { MainViewModel(get()) }
}

val detailsModule = module {

    scope(DETAIL_SCOPE) { DetailViewModel(get()) }

}

val activitiesModules = listOf(mainModule, detailsModule)

object ModuleConstants {

    const val DETAIL_SCOPE = "com.igweze.ebi.details_scope"
}