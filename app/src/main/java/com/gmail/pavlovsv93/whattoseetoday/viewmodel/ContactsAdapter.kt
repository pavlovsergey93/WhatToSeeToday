package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import android.media.Image
import android.transition.CircularPropagation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.model.Contact
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview.ContactsFragment
import com.squareup.picasso.Picasso

class ContactsAdapter(private val onClickContacts: ContactsFragment.OnClickContacts) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    private var contactList: MutableList<Contact> = mutableListOf()

    fun setContactsList(data: MutableList<Contact>?) {
        if (data != null) {
            contactList = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false) as View)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(contactList[position])
    }

    override fun getItemCount(): Int = contactList.size

    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            val card = itemView.findViewById<CardView>(R.id.contact_cardview)
            card.setOnClickListener {
                onClickContacts?.onClickContact(contact)
            }
            val iconContact = itemView.findViewById<AppCompatImageView>(R.id.contact_imageview)
            Picasso.with(itemView.context)
                .load(contact.photo)
                .centerCrop()
                .resize(400, 400)
                .placeholder(R.drawable.ic_outline_account_circle_24)
                .into(iconContact)
            val nameContact = itemView.findViewById<TextView>(R.id.contact_title)
                nameContact.text = contact.name
            val numberContact = itemView.findViewById<TextView>(R.id.contact_number)
            numberContact.text = contact.number
        }
    }
}
