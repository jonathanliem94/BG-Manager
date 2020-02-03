package com.jonathanl.bgmanager

import android.view.View
import com.jonathanl.bgmanager.ui.home.SearchViewModel
import org.junit.Assert
import org.junit.Test

class SearchViewModelTest {

    private val searchViewModelUnderTest = SearchViewModel()

    @Test
    fun `RecyclerView is GONE, Progress Bar is VISIBLE, during Search`() {
        searchViewModelUnderTest.setVisibilityDuringSearch()
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.progressBarVisibility.get())
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.recyclerViewVisibility.get())
    }

    @Test
    fun `RecyclerView is VISIBLE, Progress Bar is GONE, after Search`() {
        searchViewModelUnderTest.setVisibilityAfterSearch()
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.progressBarVisibility.get())
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.recyclerViewVisibility.get())
    }


}