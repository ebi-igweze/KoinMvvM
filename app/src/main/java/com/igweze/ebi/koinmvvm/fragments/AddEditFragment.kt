package com.igweze.ebi.koinmvvm.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.igweze.ebi.koinmvvm.R

class AddEditFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_menu, menu)
    }

    companion object {

        fun newInstance(bundle: Bundle) = AddEditFragment().apply {
            arguments = bundle
        }
    }
}