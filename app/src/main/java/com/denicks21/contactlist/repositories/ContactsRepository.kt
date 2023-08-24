package com.denicks21.contactlist.repositories

import androidx.lifecycle.LiveData
import com.denicks21.contactlist.roomdb.Contacts
import com.denicks21.contactlist.roomdb.ContactsDatabase

class ContactsRepository(private val contactsDatabase: ContactsDatabase) {

    private var list: LiveData<List<Contacts>> =
        contactsDatabase.getContactsDao().getAllContacts()

    suspend fun insertContact(contacts: Contacts) =
        contactsDatabase.getContactsDao().insertContact(contacts)

    suspend fun updateContact(contacts: Contacts) =
        contactsDatabase.getContactsDao().updateContact(contacts)

    suspend fun deleteContact(contacts: Contacts) =
        contactsDatabase.getContactsDao().deleteContact(contacts)

//    suspend fun clearContacts() = contactsDatabase.getContactsDao().clearContacts()
//
//    suspend fun deleteContactById(id: Int) =
//        contactsDatabase.getContactsDao().deleteContactId(id.toLong())

    fun getAllContacts(): LiveData<List<Contacts>> = list
}