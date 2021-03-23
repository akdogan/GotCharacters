package com.akdogan.swcharacters.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.akdogan.swcharacters.R
import com.akdogan.swcharacters.datasource.Repository
import com.akdogan.swcharacters.datasource.ServiceLocator
import com.akdogan.swcharacters.datasource.instrumentedTestChar
import com.akdogan.swcharacters.datasource.setupRemoteSourceFakeWithTestDataInstrumented
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    private lateinit var repo: Repository

    @Before
    fun setUp() {
        val remoteSource = setupRemoteSourceFakeWithTestDataInstrumented()
        val context = ApplicationProvider.getApplicationContext<Context>()
        ServiceLocator.setupTestRepository(
            app = context,
            remoteSource = remoteSource
        )
        repo = ServiceLocator.getDefaultRepository(context)
    }

    @Test
    fun test(){
        runBlocking {
            repo.fetchData()
            launchFragmentInContainer<MainFragment>(Bundle(), R.style.Theme_SWCharacters)
            delay(5000)
            onView(withId(R.id.list_item_name)).check(matches(withText(instrumentedTestChar.name)))
        }
        //assertEquals(1, 1)
    }

    @After
    fun tearDown() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        ServiceLocator.cleanUpRepository(context)
    }
}