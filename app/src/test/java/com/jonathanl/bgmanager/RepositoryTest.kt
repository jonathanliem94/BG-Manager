package com.jonathanl.bgmanager

import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.repository.Repository
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Assert
import org.junit.Test

class RepositoryTest {

    private val repositoryUnderTest: Repository = Repository()

    @Test
    fun checkMakeBoardGameSearch() {
        var result = BoardGameSearchResults()
        repositoryUnderTest.makeBoardGameSearch("Gloomhaven")
            .subscribeBy {
                result = it
            }

        Assert.assertEquals("8", result.total)
        Assert.assertNotNull(result.resultsArray)
        Assert.assertEquals(result.total, result.resultsArray.size.toString())
    }


}