package com.igweze.ebi.koinmvvm.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.igweze.ebi.koinmvvm.data.managers.ContactManager
import com.igweze.ebi.koinmvvm.data.models.ContactDetail
import com.igweze.ebi.koinmvvm.utilities.computationToUI
import com.igweze.ebi.koinmvvm.utilities.subscribeToError
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class DetailViewModel(private val contactManager: ContactManager): ViewModel() {

    enum class ContactFragment {
        EditFragment,
        DetailFragment
    }

    private val disposables by lazy { CompositeDisposable() }
    private val contactDetail = MutableLiveData<ContactDetail>()
    private val contactFragment = MutableLiveData<ContactFragment>()

    fun getContactDetail(): LiveData<ContactDetail> = contactDetail
    fun getContactFragment(): LiveData<ContactFragment> = contactFragment

    fun setFragment(fragment: ContactFragment) {
        contactFragment.value = fragment
    }

    fun setContactDetail(contactId: Int) {

        disposables.clear()

        val disposable = contactManager.getContactDetail(contactId)
                .computationToUI()
                .subscribe { contact, throwable ->
                    // check if any error occurred, else set value for contact detail
                    if (throwable != null) Timber.e("An Error occured: ${throwable.localizedMessage}")
                    else contactDetail.value = contact
                }

        disposables.add(disposable)
    }

    fun addContactDetail(contactDetail: ContactDetail) {
        contactManager.addContactDetail(contactDetail)
                .computationToUI()
                .subscribeToError()
    }

    fun updateContactDetail(contactDetail: ContactDetail) {
        contactManager.updateContactDetail(contactDetail).subscribeToError()
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}