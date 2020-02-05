package com.jonathanl.bgmanager

import android.view.View
import com.jonathanl.bgmanager.ui.search.SearchViewModel
import org.junit.Assert
import org.junit.Test

class SearchViewModelTest {

    private val searchViewModelUnderTest = SearchViewModel()

    @Test
    fun `RecyclerView is GONE, Progress Bar is VISIBLE, TextView is VISIBLE, during Search`() {
        searchViewModelUnderTest.setVisibilityDuringSearch()
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.progressBarVisibility.get())
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.recyclerViewVisibility.get())
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.textViewVisibility.get())
    }

    @Test
    fun `RecyclerView is VISIBLE, Progress Bar is GONE, TextView is GONE, after Search with Results`() {
        searchViewModelUnderTest.setVisibilityAfterSearchWithResults()
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.progressBarVisibility.get())
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.recyclerViewVisibility.get())
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.textViewVisibility.get())
    }

    @Test
    fun `RecyclerView is GONE, Progress Bar is GONE, TextView is VISIBLE, after Search with No Results`() {
        searchViewModelUnderTest.setVisibilityAfterSearchWithNoResults()
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.progressBarVisibility.get())
        Assert.assertEquals(View.GONE, searchViewModelUnderTest.recyclerViewVisibility.get())
        Assert.assertEquals(View.VISIBLE, searchViewModelUnderTest.textViewVisibility.get())
    }


}