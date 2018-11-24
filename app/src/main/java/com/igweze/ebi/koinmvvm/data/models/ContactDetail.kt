package com.igweze.ebi.koinmvvm.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "contact")
data class ContactDetail(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var firstName: String,
        var lastName: String,
        var email: String,
        var phoneNumber: String,
        var address: String)