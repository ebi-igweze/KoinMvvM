package com.igweze.ebi.koinmvvm.data.storage

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.igweze.ebi.koinmvvm.data.models.Contact
import com.igweze.ebi.koinmvvm.data.models.ContactDetail
import java.util.*

@Dao
interface ContactDao {

    @Insert
    fun insertContact(contact: ContactDetail): Long

    @Query("SELECT id, firstName, lastName, address FROM contact ORDER BY firstName ASC")
    fun getContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE id = :id")
    fun getContactById(id: Int): ContactDetail

    @Update
    fun updateContact(contact: ContactDetail)

    @Delete
    fun deleteContact(contact: ContactDetail): Int

    @Query("SELECT * FROM contact WHERE timeUpdated >= :time")
    fun getContactsByTime(time: Long): List<ContactDetail>

}