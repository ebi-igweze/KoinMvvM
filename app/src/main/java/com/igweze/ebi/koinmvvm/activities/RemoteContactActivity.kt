package com.igweze.ebi.koinmvvm.activities

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.adapters.ContactAdapter
import com.igweze.ebi.koinmvvm.utilities.ignore
import com.igweze.ebi.koinmvvm.viewmodels.RemoteContactViewModel
import kotlinx.android.synthetic.main.activity_remote_contact.*
import org.koin.android.viewmodel.ext.android.viewModel

class RemoteContactActivity : AppCompatActivity() {

    private val remoteViewModel: RemoteContactViewModel by viewModel()
    private lateinit var adapter: ContactAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_contact)

        // setup ui
        setupUI()

        // observe view model changes
        observeViewModelChanges()
    }

    private fun setupUI() {
        // set support toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setTitle(R.string.title_remote_contacts)
            setDisplayHomeAsUpEnabled(true)
        }

        // recycler view adapter and item decorator
        adapter = ContactAdapter(ArrayList()) { ignore() }
        val itemDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        // setup recycler view
        contactsRecycler.adapter = adapter
        contactsRecycler.addItemDecoration(itemDivider)
        contactsRecycler.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModelChanges() {
        remoteViewModel.getContactList().observe(this, Observer {
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
