package com.gmail.pavlovsv93.whattoseetoday.model.repo

import android.content.ComponentCallbacks
import android.content.ContentResolver
import android.database.Cursor
import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.Contact

interface InterfaceContactRepository {
    fun getContactBooks(
        contactResolver: ContentResolver,
        callbacks: Callback<MutableList<Contact>>
    )
}