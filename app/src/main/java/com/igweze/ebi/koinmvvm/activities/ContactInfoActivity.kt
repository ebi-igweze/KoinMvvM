package com.igweze.ebi.koinmvvm.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.igweze.ebi.koinmvvm.R
import com.igweze.ebi.koinmvvm.di.ModuleConstants
import com.igweze.ebi.koinmvvm.fragments.AddEditFragment
import com.igweze.ebi.koinmvvm.fragments.DetailFragment
import com.igweze.ebi.koinmvvm.viewmodels.DetailViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.bindScope
import org.koin.android.scope.ext.android.getOrCreateScope

class ContactInfoActivity : AppCompatActivity() {

    private val detailViewModel: DetailViewModel by inject()
    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_info)

        // create scope for activity
        val scope = getOrCreateScope(ModuleConstants.DETAIL_SCOPE)
        bindScope(scope)

        intent.extras?.apply {
            // set up the user interface
            setupUI(this)

            // observe view model
            observeViewModel(this)
        }
    }

    private fun setupUI(bundle: Bundle) {

        // set the toolbar
        setSupportActionBar(toolbar)
        // show back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // set back arrow click listener
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // get chosen operation for fragment
        val operation = bundle.getInt(KEY_OPERATION, OPERATION_ADD)

        // setup fragment based on operation
        setupFragment(operation, bundle)
    }

    private fun observeViewModel(bundle: Bundle) {
        // observe changes for swapping fragment view
        detailViewModel.getContactFragment().observe(this, Observer {
            when (it) {
                DetailViewModel.ContactFragment.EditFragment -> setupFragment(OPERATION_EDIT, bundle)
                DetailViewModel.ContactFragment.DetailFragment -> setupFragment(OPERATION_VIEW, bundle)
            }
        })
    }

    // set up the current fragment's view
    private fun setupFragment(operation: Int, bundle: Bundle) {
        // check if current fragment is detail fragment
        val wasDetail = ::fragment.isInitialized && fragment is DetailFragment

        fragment  = when (operation) {
            OPERATION_EDIT -> setupEditContact(bundle)
            OPERATION_VIEW -> setupViewContact(bundle)
            else -> setupAddContact(bundle)
        }

        // replace the container with fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.layoutContainer, fragment)
                .also { transaction ->
                    // if we are moving from detail fragment to edit fragment
                    // add new fragment to back stack, to go back to edit fragment
                    val shouldAddToBackStack = wasDetail
                            && fragment is AddEditFragment
                            && operation == OPERATION_EDIT

                    if (shouldAddToBackStack)
                        transaction.addToBackStack(fragment.tag)
                }.commit()
    }

    private fun setupAddContact(bundle: Bundle): Fragment {
        bundle.putInt(KEY_OPERATION, OPERATION_ADD)
        return AddEditFragment.newInstance(bundle)
    }

    private fun setupEditContact(bundle: Bundle): Fragment {
        bundle.putInt(KEY_OPERATION, OPERATION_EDIT)
        return AddEditFragment.newInstance(bundle)
    }

    private fun setupViewContact(bundle: Bundle): Fragment {
        bundle.getInt(KEY_OPERATION, OPERATION_VIEW)
        return DetailFragment.newInstance(bundle)
    }

    companion object {
        const val KEY_CONTACT_ID = "contact_id_key"
        const val KEY_OPERATION = "operation"
        const val NO_CONTACT_ID = 0

        const val OPERATION_ADD = 1
        const val OPERATION_VIEW = 2
        const val OPERATION_EDIT = 0
    }
}
