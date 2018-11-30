package com.igweze.ebi.koinmvvm.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.igweze.ebi.koinmvvm.data.managers.ContactManager
import com.igweze.ebi.koinmvvm.data.models.Contact

class MainViewModel(private val contactManager: ContactManager): ViewModel() {

    private val deleteStatus = MutableLiveData<Boolean>()

    fun getDeleteStatus(): LiveData<Boolean> = deleteStatus
    fun getContacts() = contactManager.getContacts()

    fun deleteContact(contact: Contact) {
        contactManager.deleteContact(contact.id).subscribe { count, _ ->
            if (count > 0) deleteStatus.postValue(true)
            else deleteStatus.postValue(false)
        }
    }

}