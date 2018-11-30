package com.igweze.ebi.koinmvvm.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.activities.ContactInfoActivity.Companion.KEY_CONTACT_ID
import com.igweze.ebi.koinmvvm.activities.ContactInfoActivity.Companion.KEY_OPERATION
import com.igweze.ebi.koinmvvm.activities.ContactInfoActivity.Companion.NO_CONTACT_ID
import com.igweze.ebi.koinmvvm.activities.ContactInfoActivity.Companion.OPERATION_ADD
import com.igweze.ebi.koinmvvm.activities.ContactInfoActivity.Companion.OPERATION_EDIT
import com.igweze.ebi.koinmvvm.data.models.ContactDetail
import com.igweze.ebi.koinmvvm.viewmodels.DetailViewModel
import kotlinx.android.synthetic.main.fragment_add_edit.*
import org.koin.android.ext.android.inject
import java.util.*

class AddEditFragment : Fragment() {

    private val detailViewModel: DetailViewModel by inject()

    private var operation = OPERATION_ADD
    private var contactId = NO_CONTACT_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        // set the current operation
        arguments?.apply {
            contactId = getInt(KEY_CONTACT_ID, NO_CONTACT_ID)
            operation = getInt(KEY_OPERATION, OPERATION_ADD)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (operation == OPERATION_EDIT) setupEditOperation()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.save) {
            // update or add based on operation
            if (operation == OPERATION_EDIT) updateContactInfo()
            else addContactInfo()

            requireActivity().onBackPressed()
            return true
        }

        return false
    }

    private fun setupEditOperation() {
        // set contact detail
        detailViewModel.setContactDetail(contactId)

        // observe contact detail result
        detailViewModel.getContactDetail().observe(this, Observer { detail ->
            detail?.apply {
                etFirstName.setText(firstName)
                etLastName.setText(lastName)
                etEmail.setText(email)
                etPhoneNumber.setText(phoneNumber)
                etAddress.setText(address)
            }
        })
    }

    private fun updateContactInfo() {
        val contactInfo = getContactForm()
        detailViewModel.updateContactDetail(contactInfo)
    }

    private fun addContactInfo() {
        val contactInfo = getContactForm()
        detailViewModel.addContactDetail(contactInfo)
    }

    private fun getContactForm() = ContactDetail(contactId,
            etFirstName.text.toString(),
            etLastName.text.toString(),
            etEmail.text.toString(),
            etPhoneNumber.text.toString(),
            Calendar.getInstance().time,
            etAddress.text.toString())


    companion object {

        fun newInstance(bundle: Bundle) = AddEditFragment().apply {
            arguments = bundle
        }
    }
}