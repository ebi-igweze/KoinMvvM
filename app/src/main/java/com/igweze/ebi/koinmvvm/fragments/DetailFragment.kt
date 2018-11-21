package com.igweze.ebi.koinmvvm.fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.activities.ContactInfoActivity


class DetailFragment: Fragment() {

    private var contactId = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.apply {

            contactId = getInt(ContactInfoActivity.KEY_CONTACT_ID, -1)

            val fabEdit: FloatingActionButton = view.findViewById(R.id.editButton)

            fabEdit.setOnClickListener {
                val intent = Intent(requireActivity(), ContactInfoActivity::class.java).apply {
                    putExtra(ContactInfoActivity.KEY_CONTACT_ID, contactId)
                    putExtra(ContactInfoActivity.KEY_OPERATION, ContactInfoActivity.OPERATION_EDIT)
                }

                it.context.startActivity(intent)
            }

        }

    }

    companion object {

        fun newInstance(bundle: Bundle) = DetailFragment().apply {
            arguments = bundle
        }
    }
}