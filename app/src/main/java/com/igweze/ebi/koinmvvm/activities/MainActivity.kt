package com.igweze.ebi.koinmvvm.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.adapters.ContactAdapter
import com.igweze.ebi.koinmvvm.data.models.Contact
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup ui
        setupUI()
    }

    private fun setupUI() {
        // set support toolbar
        setSupportActionBar(toolbar)

        // setup recycler
        val contacts = getContacts()
        contactsRecycler.adapter = ContactAdapter(contacts)
        contactsRecycler.layoutManager = LinearLayoutManager(this)

        addButton.setOnClickListener {
            startActivity(Intent(this, ContactInfoActivity::class.java).apply {
                putExtra(ContactInfoActivity.KEY_OPERATION, ContactInfoActivity.OPERATION_ADD)
            })
        }

    }


    private fun getContacts() = listOf(
            Contact(0, "Ebi", "Igweze", "32, Chief Collins Street, Lekki Phase 1"),
            Contact(1, "Daniel", "Adenuga", "12, Adesina Street, Iyana Ipaja"),
            Contact(2, "James", "Patrick", "2, Bisway street, Lekki Phase 1"),
            Contact(3, "John", "Ogbomanu", "8, Alakija, off Bode Thomas Street, Surulere"),
            Contact(4, "Uche", "Okonkwo", "7, harbert Marculey way, Ikoyi")
    )

}
