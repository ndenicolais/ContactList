package com.denicks21.contactlist.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.denicks21.contactlist.utils.Converters

@Database(
    entities = [Contacts::class], version = 1, exportSchema = true
)

@TypeConverters(Converters::class)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun getContactsDao() : ContactsDao


    companion object {
        private const val DB_NAME = "contactslist.db"

        @Volatile
        private var instance: ContactsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ContactsDatabase::class.java,
            DB_NAME
        ).build()

        fun close() {
            instance?.close()
            instance = null
        }
    }
}


