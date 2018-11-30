package com.igweze.ebi.koinmvvm.data.storage

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.igweze.ebi.koinmvvm.data.models.ContactDetail

@Database(entities = [ContactDetail::class], exportSchema = false, version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract val contactDao: ContactDao

    companion object {

        private const val DATABASE_NAME = "addressbook.db"

        fun getInstance(context: Context): AppDatabase = synchronized(this) { buildDatabase(context) }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigrationFrom(1)
                        .build()

    }

}