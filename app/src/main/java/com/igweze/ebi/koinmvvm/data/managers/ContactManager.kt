package com.igweze.ebi.koinmvvm.data.managers

import android.arch.lifecycle.LiveData
import com.igweze.ebi.koinmvvm.data.models.Contact
import com.igweze.ebi.koinmvvm.data.models.ContactDetail
import com.igweze.ebi.koinmvvm.data.storage.ContactDao
import com.igweze.ebi.koinmvvm.utilities.computationToUI
import com.igweze.ebi.koinmvvm.utilities.subscribeToError
import io.reactivex.Single
import java.util.*

class ContactManager(private val contactDao: ContactDao) {

    fun getContacts(): LiveData<List<Contact>> = contactDao.getContacts()

    fun getContactDetail(id: Int): Single<ContactDetail> = Single.fromCallable { contactDao.getContactById(id) }

    fun addContactDetail(contactDetail: ContactDetail): Single<Long> = Single.fromCallable { contactDao.insertContact(contactDetail) }

    fun updateContactDetail(contactDetail: ContactDetail): Single<Unit> {
        return Single.fromCallable { contactDao.updateContact(contactDetail) }
                .computationToUI() // execute query in computation thread
    }

    fun getContactsFrom(time: Date): Single<List<ContactDetail>> = Single.fromCallable { contactDao.getContactsByTime(time.time) }

    fun deleteContact(contactId: Int): Single<Int> = getContactDetail(contactId).map { contactDao.deleteContact(it) }

}