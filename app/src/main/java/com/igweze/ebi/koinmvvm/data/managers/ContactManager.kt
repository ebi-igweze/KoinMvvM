package com.igweze.ebi.koinmvvm.data.managers

import android.arch.lifecycle.LiveData
import com.igweze.ebi.koinmvvm.data.models.Contact
import com.igweze.ebi.koinmvvm.data.models.ContactDetail
import com.igweze.ebi.koinmvvm.data.storage.ContactDao
import com.igweze.ebi.koinmvvm.utilities.computationToUI
import com.igweze.ebi.koinmvvm.utilities.subscribeToError
import io.reactivex.Single

class ContactManager(private val contactDao: ContactDao) {

    fun getContacts(): LiveData<List<Contact>> = contactDao.getContacts()

    fun getContactDetail(id: Int): Single<ContactDetail> = Single.fromCallable { contactDao.getContactById(id) }

    fun addContactDetail(contactDetail: ContactDetail): Single<Long> = Single.fromCallable { contactDao.insertContact(contactDetail) }

    fun updateContactDetail(contactDetail: ContactDetail) {
        Single.fromCallable { contactDao.updateContact(contactDetail) }
                .computationToUI() // execute query in computation thread
                .subscribeToError() // handle any error outputs
    }

}