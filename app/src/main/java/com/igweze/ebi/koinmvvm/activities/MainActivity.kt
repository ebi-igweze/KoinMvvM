package com.igweze.ebi.koinmvvm.activities

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.adapters.ContactAdapter
import com.igweze.ebi.koinmvvm.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup ui
        setupUI()
    }

    private fun setupUI() {
        // set support toolbar
        setSupportActionBar(toolbar)

        // recycler view adapter and item decorator
        val adapter = ContactAdapter(ArrayList())
        val itemDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        // setup recycler view
        contactsRecycler.adapter = adapter
        contactsRecycler.addItemDecoration(itemDivider)
        contactsRecycler.layoutManager = LinearLayoutManager(this)

        addButton.setOnClickListener {
            startActivity(Intent(this, ContactInfoActivity::class.java).apply {
                putExtra(ContactInfoActivity.KEY_OPERATION, ContactInfoActivity.OPERATION_ADD)
            })
        }

        mainViewModel.getContacts().observe(this, Observer {
            val contacts = when(it) {
                null -> ArrayList()
                else -> it
            }

            if (contacts.isNotEmpty()) hideNoContacts()
            else showNoContacts()

            // setup list for recycler
            adapter.setContactList(contacts)
        })

    }

    private fun hideNoContacts() {
        contactsRecycler.bringToFront()
        contactsRecycler.visibility = View.VISIBLE
        noContactContainer.visibility = View.INVISIBLE
    }

    private fun showNoContacts() {
        noContactContainer.bringToFront()
        noContactContainer.visibility = View.VISIBLE
        contactsRecycler.visibility = View.INVISIBLE
    }

}
