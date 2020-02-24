package com.jonathanl.bgmanager.data.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/* XML unmarshalling for  Get Boardgame Info Request */

@Root(name = "boardgames", strict = false)
data class BoardGameInfo(
    @field:Element(name = "boardgame") var boardGameData: BoardGameData = BoardGameData()
)

@Root(name = "boardgame", strict = false)
data class BoardGameData(
    @field:Element(name = "yearpublished") var yearPublished: String? = null,
    @field:Element(name = "minplayers") var minPlayers: String? = null,
    @field:Element(name = "maxplayers") var maxPlayers: String? = null,
    @field:Element(name = "minplaytime") var minPlayTime: String? = null,
    @field:Element(name = "maxplaytime") var maxPlayTime: String? = null,
    @field:Element(name = "playingtime") var averagePlayTime: String? = null,
    @field:Element(name = "description") var description: String? = null,
    @field:Element(name = "thumbnail", required = false) var bgThumbnail: String? = null,
    @field:Element(name = "image", required = false) var bgImage: String? = null,
    @field:ElementList(entry = "boardgamepublisher", required = false, inline = true) var bgListOfPublishers: List<String>? = null,
    @field:ElementList(entry = "boardgamehonor", required = false, inline = true) var bgListofAwards: List<String>? = null,
    @field:ElementList(entry = "boardgamemechanic", required = false, inline = true) var bgListofMechanics: List<String>? = null,
    @field:ElementList(entry = "boardgamecategory", required = false, inline = true) var bgListofCategories: List<String>? = null,
    @field:ElementList(entry = "boardgamefamily", required = false, inline = true) var bgListofRelatedGameTypes: List<String>? = null,
    @field:ElementList(entry = "boardgamedesigner", required = false, inline = true) var bgListofGameDesigners: List<String>? = null,
    @field:ElementList(entry = "boardgameversion", required = false, inline = true) var bgListofGameVersions: List<String>? = null,
    @field:ElementList(entry = "boardgameintegration", required = false, inline = true) var bgListofRelatedGames: List<String>? = null,
    @field:ElementList(entry = "boardgameexpansion", required = false, inline = true) var bgListofGameExpansions: List<String>? = null
)