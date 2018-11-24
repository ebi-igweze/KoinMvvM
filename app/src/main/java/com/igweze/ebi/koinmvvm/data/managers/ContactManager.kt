package com.igweze.ebi.koinmvvm.data.managers

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.igweze.ebi.koinmvvm.data.models.Contact
import com.igweze.ebi.koinmvvm.data.models.ContactDetail
import com.igweze.ebi.koinmvvm.data.storage.ContactDao

class ContactManager(private val contactDao: ContactDao) {

    fun getContacts(): LiveData<List<Contact>> {
        return MutableLiveData<List<Contact>>().apply {
            value = listOf(
                    Contact(10, "Ebi", "Igweze", "32, Chief Collins Street, Lekki Phase 1"),
                    Contact(11, "Daniel", "Adenuga", "12, Adesina Street, Iyana Ipaja"),
                    Contact(12, "James", "Patrick", "2, Bisway street, Lekki Phase 1"),
                    Contact(13, "John", "Ogbomanu", "8, Alakija, off Bode Thomas Street, Surulere"),
                    Contact(14, "Uche", "Okonkwo", "7, harbert Marculey way, Ikoyi")
            )
        }
    }

    fun getContactDetail(id: Int): LiveData<ContactDetail> {
        return MutableLiveData<ContactDetail>().apply {
            value = ContactDetail(id, "Ebi", "Igweze", "ebi@gmail.co.uk", "080-999-23840",
                    "No 12, Charles Adenuga Street, Lekki Phase 1, Lagos.")
        }
    }

    fun updateContactDetail(contactDetail: ContactDetail) {
        // TODO update local db
    }

    fun addContactDetail(contactDetail: ContactDetail) {
        // TODO add to local db
    }
}