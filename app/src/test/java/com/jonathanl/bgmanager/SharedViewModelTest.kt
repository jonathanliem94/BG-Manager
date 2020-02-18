package com.jonathanl.bgmanager

import com.jonathanl.bgmanager.repository.Repository
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test

class SharedViewModelTest {

    private val repository: Repository = mock()
    private val sharedViewModelUnderTest: SharedViewModel = SharedViewModel(repository)

    @Before
    fun setUp() {

    }

    @Test
    fun `when a duplicate entry is the input, the game list should not change`() {

    }

    @Test
    fun `when a new entry is the input, the game list have that entry added to itself`() {

    }

}