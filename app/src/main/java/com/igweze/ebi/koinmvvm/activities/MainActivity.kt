package com.igweze.ebi.koinmvvm.activities

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
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

        val adapter = ContactAdapter(ArrayList())
        contactsRecycler.adapter = adapter
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

            // setup list for recycler
            adapter.setContactList(contacts)
        })

    }

}
