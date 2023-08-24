package com.denicks21.contactlist.utils

import android.content.Context
import android.util.Patterns
import android.widget.Toast

class Validation(
    val context: Context,
    private val name: String? = null,
    private val surname: String? = null,
    private val email: String? = null,
    private val phoneNumber: String? = null,
) {

    fun isNameValid(name: String? = this.name): Boolean {
        if (name == null || name.trim().isEmpty()) {
            Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun isSurnameValid(surname: String? = this.surname): Boolean {
        if (surname == null || surname.trim().isEmpty()) {
            Toast.makeText(context, "Please enter a surname", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun isEmailValid(email: String? = this.email): Boolean {
        if (email == null || email.trim().isEmpty()) {
            Toast.makeText(context, "Please enter an email", Toast.LENGTH_SHORT).show()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun isPhoneNumberValid(phoneNumber: String? = this.phoneNumber): Boolean {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            Toast.makeText(context, "Please enter a phone number", Toast.LENGTH_SHORT).show()
            return false
        } else if (!Patterns.PHONE.matcher(phoneNumber.trim()).matches()) {
            Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}