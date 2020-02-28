package com.jonathanl.bgmanager

import android.os.Bundle
import androidx.test.annotation.UiThreadTest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.jonathanl.bgmanager.ui.boardgamedetails.BoardGameDetailsFragment
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameDetailsScreenTest {

    private val testGameName = "TestGame"
    private val testGameId = "123"

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    @UiThreadTest
    fun startFragment() {
        val fragmentArgs = Bundle().apply {
            putString("gameName", testGameName)
            putString("gameId", testGameId)
        }
        val transaction = activityRule.activity.supportFragmentManager.beginTransaction()
        transaction.add(BoardGameDetailsFragment::class.java, fragmentArgs, "testFragment")
        transaction.commitNowAllowingStateLoss()
    }

    @Ignore("Can't get the fragment to appear")
    @Test
    fun testOnInit() {
        onView(withId(R.id.gameIdText)).check(matches(withText(testGameId)))
        onView(withId(R.id.gameNameText)).check(matches(withText(testGameName)))
    }

}