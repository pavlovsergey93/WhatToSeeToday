package com.gmail.pavlovsv93.whattoseetoday.model.repo

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.Contact
import kotlin.Exception

class ContactRepository : InterfaceContactRepository{

    override fun getContactBooks(
        contactResolver: ContentResolver,
        callbacks: Callback<MutableList<Contact>>
    ){
        try {
            callbacks.onSuccess(getContactList(getContactBook(contactResolver)))
        }catch (e : Exception){
            callbacks.onError(e)
        }

    }

    private fun getContactBook(contactResolver: ContentResolver): Cursor? {
        return contactResolver
            .query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
    }
    @SuppressLint("Range")
    private fun getContactList(cursorWithContacts: Cursor?): MutableList<Contact> {
        val mutableList: MutableList<Contact> = mutableListOf()
        cursorWithContacts?.let { cursor ->
            for (i in 0..cursor.count) {
                if (cursor.moveToPosition(i)) {
                    val name = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    )
                    val phone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    mutableList.add(Contact(i.toLong(),name, null, null, phone))
                }
            }
        }
        cursorWithContacts?.close()
        return mutableList
    }
}