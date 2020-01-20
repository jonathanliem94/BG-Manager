package com.jonathanl.bgmanager.network

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlRootElement

/* XML unmarshalling for Search Boardgame Request */

@XmlRootElement(name = "items")
data class BoardGameSearchResults(@XmlAttribute val total: String = "",
                             @XmlAttribute val termsofuse: String = "")
{

    @XmlElement(name = "item")
    val resultsArray: List<BoardGameResult> = mutableListOf(BoardGameResult())

}

data class BoardGameResult(@XmlAttribute(name = "type") val type: String = "",
                      @XmlAttribute(name = "id") val gameId: String = "")
{

    @XmlElement(name = "name")
    val boardGameNameResult: BoardGameName = BoardGameName()

    @XmlElement(name = "yearpublished")
    val yearPublished: YearOfPublication = YearOfPublication()

}

data class BoardGameName(@XmlAttribute(name = "type") val type: String = "",
                         @XmlAttribute(name = "value") val gameName: String = "")

data class YearOfPublication(@XmlAttribute(name = "value") val year: String = "")


/* XML unmarshalling for  */