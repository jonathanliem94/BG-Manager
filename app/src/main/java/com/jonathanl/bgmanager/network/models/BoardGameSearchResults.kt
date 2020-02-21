package com.jonathanl.bgmanager.network.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/* XML unmarshalling for Search Boardgame Request */

@Root(name = "items", strict = false)
data class BoardGameSearchResults(
    @field:Attribute(name = "total") var total: String = "",
    @field:ElementList(name = "item", inline = true, required = false) var resultsArray: List<BoardGameResult> = arrayListOf())

@Root(name = "item", strict = false)
data class BoardGameResult(
    @field:Attribute(name = "id") var gameId: String = "",
    @field:Element(name = "name") var boardGameNameResult: BoardGameName = BoardGameName()
)

@Root(name = "name", strict = false)
data class BoardGameName(
    @field:Attribute(name = "value") var gameName: String = "")
