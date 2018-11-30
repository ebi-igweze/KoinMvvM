package com.igweze.ebi.koinmvvm.activities

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.adapters.ContactAdapter
import com.igweze.ebi.koinmvvm.services.SyncService
import com.igweze.ebi.koinmvvm.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by inject()

    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup ui
        setupUI()

        // observe view model
        observeViewModel()

        // start sync service
        val serviceIntent = Intent(this, SyncService::class.java).apply {
            putExtra(SyncService.KEY_START_STATE, SyncService.VALUE_BACKGROUND)
        }
        startService(serviceIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.syncedContacts -> showRemoteContacts()
            else -> false
        }
    }

    private fun setupUI() {
        // set support toolbar
        setSupportActionBar(toolbar)

        // recycler view adapter and item decorator
        adapter = ContactAdapter(ArrayList()) { mainViewModel.deleteContact(it)}
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

    }

    private fun observeViewModel() {

        // observe delete status
        mainViewModel.getDeleteStatus().observe(this, Observer {
            it?.apply {
                val message =
                        if (this) "Contact Deleted Successfully"
                        else "Unable to Delete Contact, please try again"

                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            }
        })

        // observe contact list
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

    private fun showRemoteContacts(): Boolean {
        startActivity(Intent(this, RemoteContactActivity::class.java))
        return true
    }

}
