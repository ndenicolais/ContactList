package com.denicks21.contactlist.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denicks21.contactlist.repositories.ContactsRepository
import com.denicks21.contactlist.roomdb.Contacts
import com.denicks21.contactlist.roomdb.ContactsDatabase
import kotlinx.coroutines.launch

class ContactViewModel(context: Context, private val lifecycleOwner: LifecycleOwner) : ViewModel() {
    private var repository: ContactsRepository
    private var database: ContactsDatabase

    var mutableContact = mutableStateListOf<Contacts>()

    init {
        database = ContactsDatabase(context = context)
        repository = ContactsRepository(contactsDatabase = database)
    }

    fun insertContact(contacts: Contacts) {
        viewModelScope.launch {
            repository.insertContact(contacts = contacts)
        }
    }

    fun updateContact(contacts: Contacts) {
        viewModelScope.launch {
            repository.updateContact(contacts = contacts)
        }
    }

    fun deleteContact(contacts: Contacts) {
        viewModelScope.launch {
            repository.deleteContact(contacts = contacts)
        }
    }

//    fun deleteContactById(id: Int) {
//        viewModelScope.launch {
//            repository.deleteContactById(id = id)
//        }
//    }
//
//    fun clearContacts() {
//        viewModelScope.launch {
//            repository.clearContacts()
//        }
//    }

    fun getAllContacts() {
        repository.getAllContacts().observe(lifecycleOwner) {
            mutableContact.clear()
            mutableContact.addAll(it)
        }
    }
}