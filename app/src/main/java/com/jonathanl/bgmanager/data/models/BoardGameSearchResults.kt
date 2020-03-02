package com.jonathanl.bgmanager.data.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/* XML unmarshalling for Search Boardgame Request */

@Root(name = "items", strict = false)
data class BoardGameSearchResults(
    @field:Attribute(name = "total") var total: String = "",
    @field:ElementList(name = "item", inline = true, required = false) var resultsArray: List<BoardGameSearchId> = arrayListOf())

@Root(name = "item", strict = false)
data class BoardGameSearchId(
    @field:Attribute(name = "id") var gameId: String = "",
    @field:Element(name = "name") var boardGameSearchNameResult: BoardGameSearchName = BoardGameSearchName()
)

@Root(name = "name", strict = false)
data class BoardGameSearchName(
    @field:Attribute(name = "value") var gameName: String = "")
