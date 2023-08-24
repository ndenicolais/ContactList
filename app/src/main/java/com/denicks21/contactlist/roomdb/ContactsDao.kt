package com.denicks21.contactlist.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denicks21.contactlist.roomdb.Contacts

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contacts: Contacts)

    @Update
    suspend fun updateContact(contacts: Contacts)

    @Delete
    suspend fun deleteContact(contacts: Contacts)

    @Query("SELECT * FROM ContactListTable ORDER BY id")
    fun getAllContacts(): LiveData<List<Contacts>>

    @Query("DELETE FROM ContactListTable")
    suspend fun clearContacts()

    @Query("DELETE FROM ContactListTable WHERE id=:id")
    suspend fun deleteContactId(id: Long)
}