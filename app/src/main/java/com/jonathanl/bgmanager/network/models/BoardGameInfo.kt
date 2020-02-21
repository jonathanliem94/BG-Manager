package com.jonathanl.bgmanager.network.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementArray
import org.simpleframework.xml.Root

/* XML unmarshalling for  Get Boardgame Info Request */

@Root(name = "boardgames", strict = false)
data class BoardGameInfo(
    @field:Element(name = "boardgame") var boardGameMonolith: BoardGameMonolith = BoardGameMonolith()
)

data class BoardGameMonolith(
    @field:Element(name = "yearpublished") var yearPublished: String? = "",
    @field:Element(name = "minplayers") var minPlayers: String? = "",
    @field:Element(name = "maxplayers") var maxPlayers: String? = "",
    @field:Element(name = "minplaytime") var minPlayTime: String? = "",
    @field:Element(name = "maxplaytime") var maxPlayTime: String? = "",
    @field:Element(name = "playingtime") var averagePlayTime: String? = "",
    @field:Element(name = "description") var description: String? = "",
    @field:Element(name = "thumbnail") var bgThumbnail: String? = null,
    @field:Element(name = "image") var bgImage: String? = null,
    @field:ElementArray(name = "boardgamepublisher", required = false) var bgListOfPublishers: List<String>? = null,
    @field:ElementArray(name = "boardgamehonor", required = false) var bgListofAwards: List<String>? = null,
    @field:ElementArray(name = "boardgamemechanic", required = false) var bgListofMechanics: List<String>? = null,
    @field:ElementArray(name = "boardgamecategory", required = false) var bgListofCategories: List<String>? = null,
    @field:ElementArray(name = "boardgamefamily", required = false) var bgListofRelatedGameTypes: List<String>? = null,
    @field:ElementArray(name = "boardgamedesigner", required = false) var bgListofGameDesigners: List<String>? = null,
    @field:ElementArray(name = "boardgameversion", required = false) var bgListofGameVersions: List<String>? = null,
    @field:ElementArray(name = "boardgameintegration", required = false) var bgListofRelatedGames: List<String>? = null,
    @field:ElementArray(name = "boardgameexpansion", required = false) var bgListofGameExpansions: List<String>? = null
)