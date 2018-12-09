package com.igweze.ebi.koinmvvm.managers

import com.igweze.ebi.koinmvvm.RxSchedulerRule
import com.igweze.ebi.koinmvvm.data.managers.ContactManager
import com.igweze.ebi.koinmvvm.data.models.ContactDetail
import com.igweze.ebi.koinmvvm.data.storage.ContactDao
import org.junit.ClassRule
import org.junit.Test
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.*
import java.util.*


class ContactManagerTest: KoinTest {

    companion object {
        // ensures android schedulers are present before test execution
        @ClassRule @JvmField val rxSchedulerRule = RxSchedulerRule()
    }

    @Test
    fun `should return the right contact`() {
        // Arrange
        val contactDao = mock(ContactDao::class.java)
        val manager = ContactManager(contactDao)
        val contactDetail = ContactDetail(1, "First", "Last", "email", "2912394023", Calendar.getInstance().time, "Address")
        `when`(contactDao.getContactById(anyInt())).thenReturn(contactDetail)


        // Act
        val testObserver = manager.getContactDetail(12).test()
        testObserver.awaitTerminalEvent()


        // Assert
        testObserver
                .assertNoErrors()
                .assertValue { c -> c == contactDetail }

    }

    @Test
    fun `should call data-store delete with correct parameter`() {
        // Arrange
        val contactDao = mock(ContactDao::class.java)
        val manager = ContactManager(contactDao)
        val expectedId = 12
        val contactDetail = ContactDetail(expectedId, "First", "Last", "email", "2912394023", Calendar.getInstance().time, "Address")
        `when`(contactDao.getContactById(anyInt())).thenReturn(contactDetail)

        // Act
        val testObservable= manager.deleteContact(expectedId).test()
        testObservable.awaitTerminalEvent()

        // Assert
        verify(contactDao).getContactById(expectedId)
        verify(contactDao).deleteContact(contactDetail)
    }


    @Test
    fun `should return correct new contact Id when insert is called`() {
        // Arrange
        val expectedContactID = 12L
        val contactDao = mock(ContactDao::class.java)
        val manager = ContactManager(contactDao)
        val contactDetail = ContactDetail(1, "First", "Last", "email", "2912394023", Calendar.getInstance().time, "Address")
        `when`(contactDao.insertContact(contactDetail)).thenReturn(expectedContactID)


        // Act
        val testObserver = manager.addContactDetail(contactDetail).test()
        testObserver.awaitTerminalEvent()


        // Assert
        testObserver
                .assertNoErrors()
                .assertValue { it == expectedContactID }
    }


    @Test
    fun `should update correct values of contact when update is called`() {
        // Arrange
        val contactDao = mock(ContactDao::class.java)
        val contactDetail = ContactDetail(1, "First", "Last", "email", "2912394023", Calendar.getInstance().time, "Address")
        val manager = ContactManager(contactDao)


        // Act
        val testObservable = manager.updateContactDetail(contactDetail).test()
        testObservable.awaitTerminalEvent()

        // Assert
        verify(contactDao).updateContact(contactDetail)
    }

    @Test
    fun `should invoke correct function to get contacts from specific time`() {
        // Arrange
        val time = Calendar.getInstance().time
        val contactDao = mock(ContactDao::class.java)
        val manager = ContactManager(contactDao)

        // Act
        val testObservable = manager.getContactsFrom(time).test()
        testObservable.awaitTerminalEvent()

        // Assert
        verify(contactDao).getContactsByTime(time.time)
    }


}