package com.jonathanl.bgmanager

import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.repository.Repository
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class RepositoryTest {

    private val repositoryUnderTest: Repository = Repository()

    @Ignore("Network call is not mocked")
    @Test
    fun checkMakeBoardGameSearch() {
        var result = BoardGameSearchResults()
        repositoryUnderTest.makeBoardGameSearch("Gloomhaven")
            .subscribeBy {
                result = it
            }
        Assert.assertEquals("9", result.total)
        Assert.assertNotNull(result.resultsArray)
        Assert.assertEquals(result.total, result.resultsArray.size.toString())
    }


}