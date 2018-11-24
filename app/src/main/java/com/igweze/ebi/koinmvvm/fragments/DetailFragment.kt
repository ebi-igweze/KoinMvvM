package com.igweze.ebi.koinmvvm.fragments

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.activities.ContactInfoActivity.Companion.KEY_CONTACT_ID
import com.igweze.ebi.koinmvvm.activities.ContactInfoActivity.Companion.NO_CONTACT_ID
import com.igweze.ebi.koinmvvm.viewmodels.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.ext.android.inject


class DetailFragment: Fragment() {

    private val detailViewModel: DetailViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.apply { setupView(this) }

    }

    private fun setupView(bundle: Bundle) {
        val contactId = bundle.getInt(KEY_CONTACT_ID, NO_CONTACT_ID)

        // close activity if no contact id was passed
        if (contactId == NO_CONTACT_ID) requireActivity().finish()
        else detailViewModel.setContactDetail(contactId)

        editButton.setOnClickListener {
            // show edit screen
            detailViewModel.setFragment(DetailViewModel.ContactFragment.EditFragment)
        }

        // get and set contact details
        detailViewModel.getContactDetail().observe(this, Observer {
            it?.apply {
                tvEmail.text = email
                tvAddress.text = address
                tvPhoneNumber.text = phoneNumber
                tvFullName.text = getString(R.string.full_name_format, firstName, lastName)
            }
        })

    }

    companion object {

        fun newInstance(bundle: Bundle) = DetailFragment().apply {
            arguments = bundle
        }
    }
}