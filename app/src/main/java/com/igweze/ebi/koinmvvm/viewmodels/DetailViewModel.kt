package com.igweze.ebi.koinmvvm.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.igweze.ebi.koinmvvm.data.managers.ContactManager
import com.igweze.ebi.koinmvvm.data.models.ContactDetail

class DetailViewModel(private val contactManager: ContactManager): ViewModel() {

    enum class ContactFragment {
        EditFragment,
        DetailFragment
    }

    private val contactFragment = MutableLiveData<ContactFragment>()
    fun getContactFragment(): LiveData<ContactFragment> = contactFragment

    fun setFragment(fragment: ContactFragment) {
        contactFragment.value = fragment
    }

    fun getContactDetail(contactId: Int) = contactManager.getContactDetail(contactId)

    fun addContactDetail(contactDetail: ContactDetail) = contactManager.addContactDetail(contactDetail)

    fun updateContactDetail(contactDetail: ContactDetail) = contactManager.updateContactDetail(contactDetail)

}