package com.jonathanl.bgmanager.data

import android.content.Context
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

    companion object {
        fun createDatabaseAccessService(context: Context): GameListDbService {
            return Room.databaseBuilder(
                context,
                GameListDbService::class.java,
                "BG Manager Database")
                .build()
        }
    }

}
