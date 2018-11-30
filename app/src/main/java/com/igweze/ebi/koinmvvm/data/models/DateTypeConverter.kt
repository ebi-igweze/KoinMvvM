package com.igweze.ebi.koinmvvm.data.models

import android.arch.persistence.room.TypeConverter
import java.util.*

open class DateTypeConverter {

    @TypeConverter
    fun toLong(time: Date): Long = time.time

    @TypeConverter
    fun toDate(long: Long): Date = Date(long)
}