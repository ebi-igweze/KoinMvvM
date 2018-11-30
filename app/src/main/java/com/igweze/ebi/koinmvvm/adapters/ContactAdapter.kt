package com.igweze.ebi.koinmvvm.adapters

import android.content.Intent
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.activities.ContactInfoActivity
import com.igweze.ebi.koinmvvm.data.models.Contact
import android.support.v7.widget.PopupMenu


class ContactAdapter(private var contacts: List<Contact>, private val onDelete: (Contact) -> Unit): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_item_contact, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    fun setContactList(list: List<Contact>) {
        contacts = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private val context = view.context
        private val contactFullName = view.findViewById<TextView>(R.id.contactFullName)
        private val contactAddress = view.findViewById<TextView>(R.id.contactAddress)
        private val moreButton = view.findViewById<ImageView>(R.id.moreBtn)

        fun bind(contact: Contact) {
            contactFullName.text = context.getString(R.string.full_name_format, contact.firstName, contact.lastName)
            contactAddress.text = contact.address

            // set onclick listener
            itemView.setOnClickListener {
                context.startActivity(Intent(context, ContactInfoActivity::class.java).apply {
                    putExtra(ContactInfoActivity.KEY_CONTACT_ID, contact.id)
                    putExtra(ContactInfoActivity.KEY_OPERATION, ContactInfoActivity.OPERATION_VIEW)
                })
            }

            // set click listener for more button
            moreButton.setOnClickListener {
                val contextThemeWrapper = ContextThemeWrapper(context, R.style.PopupMenuOverlapAnchor)
                val popup = PopupMenu(contextThemeWrapper, moreButton)

                // inflate popup menu
                popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)

                // set popup menu click listener
                popup.setOnMenuItemClickListener { menu ->
                    return@setOnMenuItemClickListener when (menu.itemId) {
                        R.id.edit -> {
                            // show the info activity
                            context.startActivity(Intent(context, ContactInfoActivity::class.java).apply {
                                putExtra(ContactInfoActivity.KEY_CONTACT_ID, contact.id)
                                putExtra(ContactInfoActivity.KEY_OPERATION, ContactInfoActivity.OPERATION_EDIT)
                            })

                            true
                        }
                        R.id.delete -> {
                            // invoke callback
                            onDelete(contact)
                            true
                        }
                        else -> false
                    }
                }

                // show the popup
                popup.show()
            }
        }
    }
}