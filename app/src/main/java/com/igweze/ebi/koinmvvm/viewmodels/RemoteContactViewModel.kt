package com.igweze.ebi.koinmvvm.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.igweze.ebi.koinmvvm.data.managers.ContactServerManager
import com.igweze.ebi.koinmvvm.data.models.Contact
import com.igweze.ebi.koinmvvm.utilities.computationToUI

class RemoteContactViewModel(private val manager: ContactServerManager): ViewModel() {

    private val contacts = MutableLiveData<List<Contact>>()
    fun getContactList(): LiveData<List<Contact>> {
        // set initial value as null
        contacts.value = null

        manager.getContacts()
                .computationToUI()
                .subscribe { result, _ -> contacts.postValue(result) }

        return contacts
    }


}