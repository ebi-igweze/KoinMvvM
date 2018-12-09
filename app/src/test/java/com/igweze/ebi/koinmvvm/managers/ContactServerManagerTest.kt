package com.igweze.ebi.koinmvvm.managers

import com.google.gson.Gson
import com.igweze.ebi.koinmvvm.data.managers.ContactServerManager
import com.igweze.ebi.koinmvvm.data.managers.SharedPreferenceManager
import com.igweze.ebi.koinmvvm.data.models.ContactDetail
import com.nhaarman.mockitokotlin2.*
import org.junit.Test
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

class ContactServerManagerTest: KoinTest {

    @Test
    fun `should add all items, when server's contact-list is null`() {
        // Arrange
        val prefManager: SharedPreferenceManager = mock()
        Mockito.`when`(prefManager.getString(anyString(), anyOrNull())).thenReturn(null)
        val manager = ContactServerManager(prefManager)

        // Act
        val currentTime = Calendar.getInstance().time
        val newContact = ContactDetail(1, "First", "Last", "email", "2912394023", Calendar.getInstance().time, "Address")
        val newList = listOf(newContact)
        val expectedValue = Gson().toJson(newList)
        manager.syncContacts(currentTime, newList)


        // Assert
        verify(prefManager).getString(anyString(), anyOrNull())
        verify(prefManager).saveString(anyString(), eq(expectedValue))
    }

    @Test
    fun `should add all items, when server's contact-list is empty`() {
        // Arrange
        val prefManager: SharedPreferenceManager = mock()
        `when`(prefManager.getString(anyString(), anyOrNull())).thenReturn(emptyArray)
        val manager = ContactServerManager(prefManager)

        // Act
        val currentTime = Calendar.getInstance().time
        val newContact = ContactDetail(1, "First", "Last", "email", "2912394023", Calendar.getInstance().time, "Address")
        val newList = listOf(newContact)
        val expectedValue = Gson().toJson(newList)
        manager.syncContacts(currentTime, newList)


        // Assert
        verify(prefManager).getString(anyString(), anyOrNull())
        verify(prefManager).saveString(anyString(), eq(expectedValue))
    }

    @Test
    fun `should update items, when server's contact-list is not empty`() {
        // Arrange
        val prefManager: SharedPreferenceManager = mock()
        `when`(prefManager.getString(anyString(), anyOrNull())).thenReturn(arrayWithValue)
        val manager = ContactServerManager(prefManager)

        // Act
        val newContact = ContactDetail(1, "First", "Last", "email", "2912394023", Calendar.getInstance().time, "Address")
        val newList = listOf(newContact)
        val actual = Gson().toJson(newList)
        manager.syncContacts(currentTime, newList)


        // Assert
        verify(prefManager).getString(anyString(), anyOrNull())
        verify(prefManager).saveString(anyString(), eq(actual))
    }

    @Test
    fun `should return empty array list when preference manager return empty array`() {
        // Arrange
        val prefManager: SharedPreferenceManager = mock()
        `when`(prefManager.getString(anyString(), anyOrNull())).thenReturn(emptyArray)
        val manager = ContactServerManager(prefManager)

        // Act
        val testObservable = manager.getContacts().test()
        testObservable.awaitTerminalEvent()

        // Assert
        verify(prefManager).getString(anyString(), anyOrNull())
        testObservable
                .assertNoErrors()
                .assertValue { it.isEmpty() }
    }

    @Test
    fun `should return a non-empty array list when preference manager return empty array`() {
        // Arrange
        val prefManager: SharedPreferenceManager = mock()
        `when`(prefManager.getString(anyString(), anyOrNull())).thenReturn(arrayWithValue)
        val manager = ContactServerManager(prefManager)

        // Act
        val testObservable = manager.getContacts().test()
        testObservable.awaitTerminalEvent()

        // Assert
        verify(prefManager).getString(anyString(), anyOrNull())
        testObservable
                .assertNoErrors()
                .assertValue { it.isNotEmpty() }
    }

    companion object {

        private const val emptyArray = "[]"
        private const val arrayWithValue = """[{"id":1,"firstName":"First","lastName":"Last","email":"email","phoneNumber":"2912394023","timeUpdated":"Dec 7, 2018 6:52:55 AM","address":"Address"}]"""
        private val currentTime = Calendar.getInstance().time
        // get date to previous month
        private val previousMonth = Calendar.getInstance().let { calendar ->
            val currentMonth = calendar.get(Calendar.MONTH)
            calendar.set(Calendar.MONTH, currentMonth - 1)
            return@let calendar.time;
        }
    }
}