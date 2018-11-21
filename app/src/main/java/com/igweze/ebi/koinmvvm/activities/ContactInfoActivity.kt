package com.igweze.ebi.koinmvvm.activities

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.fragments.AddEditFragment
import com.igweze.ebi.koinmvvm.fragments.DetailFragment
import kotlinx.android.synthetic.main.activity_main.*

class ContactInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)

        // set the toolbar
        setSupportActionBar(toolbar)
        // show back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // set back arrow click listener
        toolbar.setNavigationOnClickListener { onBackPressed() }


        intent.extras?.apply {
            val operation = this.getInt(KEY_OPERATION, OPERATION_ADD)

            val fragment  = when (operation) {
                OPERATION_EDIT -> setupEditContact(this)
                OPERATION_VIEW -> setupViewContact(this)
                else -> setupAddContact(this)
            }

            // set
            supportFragmentManager.beginTransaction()
                    .replace(R.id.layoutContainer, fragment).also {
                        if (fragment is AddEditFragment && operation == OPERATION_EDIT)
                            it.addToBackStack(fragment.tag)
                    }.commit()

        }

    }

    private fun setupAddContact(bundle: Bundle): Fragment = AddEditFragment.newInstance(bundle)
    private fun setupEditContact(bundle: Bundle): Fragment = AddEditFragment.newInstance(bundle)
    private fun setupViewContact(bundle: Bundle): Fragment = DetailFragment.newInstance(bundle)

    companion object {
        const val KEY_CONTACT_ID = "contact_id_key"
        const val KEY_OPERATION = "operation"

        const val OPERATION_ADD = 1
        const val OPERATION_VIEW = 2
        const val OPERATION_EDIT = 0
    }
}
