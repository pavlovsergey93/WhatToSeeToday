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
            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
    }

    @SuppressLint("Range")
    private fun getContactList(cursorWithContacts: Cursor?): MutableList<Contact> {
        val mutableList: MutableList<Contact> = mutableListOf()
        cursorWithContacts?.let { cursor ->
            for (i in 0 .. cursor.count) {
                if (cursor.moveToPosition(i)) {
                    //CommonDataKinds.Phone.NUMBER
                    val uid = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val photo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                    val number = cursor?.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    
                    mutableList.add(Contact(uid = uid , name = name, photo = photo, number = number))
                }
            }
        }
        cursorWithContacts?.close()
        return mutableList
    }
}