package com.jonathanl.bgmanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameListEntry(
    @PrimaryKey val gameId: String,
    @ColumnInfo(name = "game_name") val gameName: String
    )