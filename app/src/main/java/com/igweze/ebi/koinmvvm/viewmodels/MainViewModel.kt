package com.igweze.ebi.koinmvvm.viewmodels

import android.arch.lifecycle.ViewModel
import com.igweze.ebi.koinmvvm.data.managers.ContactManager

class MainViewModel(private val contactManager: ContactManager): ViewModel() {

    fun getContacts() = contactManager.getContacts()

}