package com.igweze.ebi.koinmvvm.data.managers

import com.google.gson.Gson
import com.igweze.ebi.koinmvvm.data.models.Contact
import com.igweze.ebi.koinmvvm.data.models.ContactDetail
import io.reactivex.Single
import java.util.*

/**
* NOTE: [ContactServerManager] Class is actually meant to communicate with the server,
* for simplicity I have decided to mock this implementation.
* In a real application, this methods will contain network calls
* to a remote server.
* */
class ContactServerManager(private val preferenceManager: SharedPreferenceManager) {

    fun getContacts(): Single<List<Contact>> = Single.fromCallable {
        val listAsString = preferenceManager.getString(CONTACT_LIST)
        return@fromCallable when (listAsString) {
            null, "", " "-> ArrayList()
            else -> Gson().fromJson(listAsString, Array<Contact>::class.java).toList()
        }
    }

    fun getLastUpdate(): Date {
        val lastUpdated = preferenceManager.getNumber(LAST_UPDATE, NO_LAST_UPDATE)
        return  Date(lastUpdated)
    }

    private fun setLastUpdated(currentTime: Date) = preferenceManager.saveNumber(LAST_UPDATE, currentTime.time)

    fun syncContacts(currentTime: Date, updateList: List<ContactDetail>) {
        // return early if nothing to update
        if (updateList.isEmpty()) return

        val listAsString = preferenceManager.getString(CONTACT_LIST)
        when(listAsString) {
            null -> addAllContacts(updateList)
            else -> addAndUpdateContacts(updateList, listAsString)
        }

        setLastUpdated(currentTime)
    }

    private fun addAllContacts(contacts: List<ContactDetail>) {
        val gson = Gson()
        val listAsString = gson.toJson(contacts)
        preferenceManager.saveString(CONTACT_LIST, listAsString)
    }

    private fun addAndUpdateContacts(contactsWithUpdates: List<ContactDetail>, existingContactsAsString: String) {
        val gson = Gson()
        val existingContacts = gson.fromJson(existingContactsAsString, Array<ContactDetail>::class.java)

        // remove updated contactsWithUpdates
        val filterUpdates = { contact: ContactDetail ->
            val existingContact = contactsWithUpdates.firstOrNull { c -> c.id == contact.id }
            when (existingContact) {
                null -> true
                else -> false
            }
        }

        // filter out all updated contacts in existing list
        val contactsWithOutUpdates =  existingContacts.toList().filter(filterUpdates)
        // combine both list of contacts
        val completeList = contactsWithUpdates + contactsWithOutUpdates
        // generate string and save to file
        val completeListAsString = gson.toJson(completeList)
        preferenceManager.saveString(CONTACT_LIST, completeListAsString)

    }

    companion object {
        private const val NO_LAST_UPDATE = 0L
        private const val LAST_UPDATE = "last_date_updated"

        private const val CONTACT_LIST = "contact_list"
    }
}