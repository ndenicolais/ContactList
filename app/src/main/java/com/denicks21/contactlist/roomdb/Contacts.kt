package com.denicks21.contactlist.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity(tableName = "ContactListTable")
data class Contacts(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "image")
    var image: ByteArray? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "surname")
    var surname: String? = null,

    @ColumnInfo(name = "city")
    var city: String? = null,

    @ColumnInfo(name = "email")
    var email: String? = null,

    @ColumnInfo(name = "phoneNumber")
    var phoneNumber: String? = null,

    @ColumnInfo(name = "birthdate")
    var birthdate: LocalDate? = null,

    @ColumnInfo(name = "isActive")
    var isActive: Boolean? = null,

    ) : Serializable {
    constructor(
        image:ByteArray, name: String, surname: String, city: String, email: String, phoneNumber: String, birthdate: LocalDate
    ) : this(
        image = image,
        name = name,
        surname = surname,
        city = city,
        email = email,
        phoneNumber = phoneNumber,
        birthdate = birthdate,
        isActive = true,
    )
}