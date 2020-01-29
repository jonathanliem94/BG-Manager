package com.jonathanl.bgmanager.network

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/* XML unmarshalling for Search Boardgame Request */

@Root(name = "items", strict = false)
data class BoardGameSearchResults(
    @field:Attribute(name = "total") var total: String = "",
    @field:Attribute(name = "termsofuse") var termsofuse: String = "",
    @field:ElementList(name = "item", inline = true) var resultsArray: List<BoardGameResult> = arrayListOf())

@Root(name = "item", strict = false)
data class BoardGameResult(
    @field:Attribute(name = "type") var type: String = "",
    @field:Attribute(name = "id") var gameId: String = "",
    @field:Element(name = "name") var boardGameNameResult: BoardGameName = BoardGameName(),
    @field:Element(name = "yearpublished", required = false) var yearPublished: YearOfPublication = YearOfPublication())

@Root(name = "name", strict = false)
data class BoardGameName(
    @field:Attribute(name = "type") var type: String = "",
    @field:Attribute(name = "value") var gameName: String = "")

@Root(name = "yearpublished", strict = false)
data class YearOfPublication(
    @field:Attribute(name = "value") var year: String = "")

/* XML unmarshalling for  */