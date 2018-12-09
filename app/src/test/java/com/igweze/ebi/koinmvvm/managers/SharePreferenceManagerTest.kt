package com.igweze.ebi.koinmvvm.managers

import android.content.Context
import android.content.SharedPreferences
import com.igweze.ebi.koinmvvm.data.managers.SharedPreferenceManager
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Test


class SharePreferenceManagerTest {

    @Test
    fun `should return a value when shared preference produces a value`() {
        // Arrange
        val expectedString = "expected value"
        val expectedNumber = 12L
        val sharedPreferences: SharedPreferences = mock {
            on { getString(any(), anyOrNull()) } doReturn expectedString
            on { getLong(any(), any()) } doReturn expectedNumber
        }
        val context: Context = mock {
            on { getSharedPreferences(any(), any()) } doReturn sharedPreferences
        }
        val manager = SharedPreferenceManager(context)


        // Act
        val stringKey = "Some string Key"
        val numberKey = "Some number key"
        val actualString = manager.getString(stringKey)
        val actualNumber = manager.getNumber(numberKey)


        // Assert
        verify(context, times(2)).getSharedPreferences(any(), any())
        verify(sharedPreferences).getString(stringKey, null)
        verify(sharedPreferences).getLong(numberKey, 0L)
        assertNotNull(actualString)
        assertSame(expectedString, actualString)
        assertSame(expectedNumber, actualNumber)
    }


    @Test
    fun `should return default value when preferences returns default`() {
        // Arrange
        val expectedDefaultString = "expected default"
        val expectedDefaultNumber = 12L
        val sharedPreferences: SharedPreferences = mock {
            on { getString(any(), anyOrNull()) } doReturn expectedDefaultString
            on { getLong(any(), any()) } doReturn expectedDefaultNumber
        }
        val context: Context = mock {
            on { getSharedPreferences(any(), any()) } doReturn sharedPreferences
        }
        val manager = SharedPreferenceManager(context)


        // Act
        val stringKey = "Some string Key"
        val numberKey = "Some number key"
        val actualString = manager.getString(stringKey, expectedDefaultString)
        val actualNumber = manager.getNumber(numberKey, expectedDefaultNumber)



        // Assert
        verify(context, times(2)).getSharedPreferences(any(), any())
        verify(sharedPreferences).getString(stringKey, expectedDefaultString)
        verify(sharedPreferences).getLong(numberKey, expectedDefaultNumber)
        assertNotNull(actualString)
        assertSame(expectedDefaultString, actualString)
        assertSame(expectedDefaultNumber, actualNumber)
    }


    @Test
    fun `should store values that are passed`() {
        // Arrange
        val expectedString = "expected stored value"
        val expectedNuber = 17L
        val editor: SharedPreferences.Editor = mock {
            on { putString(any(), any()) } doReturn mock
            on { putLong(any(), any()) } doReturn mock
        }

        val sharedPreferences: SharedPreferences = mock {
            on { edit() } doReturn editor
        }
        val context: Context = mock {
            on { getSharedPreferences(any(), any()) } doReturn sharedPreferences
        }
        val manager = SharedPreferenceManager(context)


        // Act
        manager.saveString("string", expectedString)
        manager.saveNumber("number", expectedNuber)


        // Assert
        verify(context, times(2)).getSharedPreferences(any(), any())
        verify(sharedPreferences, times(2)).edit()
        verify(editor).putString("string", expectedString)
        verify(editor).putLong("number", expectedNuber)
        verify(editor, times(2)).commit()
    }
}