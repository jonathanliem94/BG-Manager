package com.jonathanl.bgmanager.data

import androidx.room.*
import com.jonathanl.bgmanager.data.models.GameListEntry
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface GameListDatabaseDAO {

    @Query("SELECT * FROM GameListEntry")
    fun getAllGameListEntry(): Observable<MutableList<GameListEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameListEntry(gameListEntry: MutableList<GameListEntry>): Completable

    @Delete
    fun deleteGameListEntry(gameListEntry: GameListEntry): Completable
}

@Database(entities = [GameListEntry::class], version = 1)
abstract class GameListDbService : RoomDatabase() {

    abstract fun getDatabaseAccessService(): GameListDatabaseDAO

}
