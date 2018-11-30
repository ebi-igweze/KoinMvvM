package com.igweze.ebi.koinmvvm.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import java.util.*

@Entity(tableName = "contact")
@TypeConverters(DateTypeConverter::class)
data class ContactDetail(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var firstName: String,
        var lastName: String,
        var email: String,
        var phoneNumber: String,
        var timeUpdated: Date,
        var address: String)