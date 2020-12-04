package ru.mikov.test.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import ru.mikov.test.data.local.entities.Card


@Dao
interface CardsDao : BaseDao<Card> {

    @Query(
        """
        SELECT * FROM cards
    """
    )
    suspend fun getAllCards(): List<Card>

    @Query(
        """
        SELECT * FROM cards
    """
    )
    fun getCards(): DataSource.Factory<Int, Card>

    @RawQuery(observedEntities = [Card::class])
    fun findCardsByRaw(simpleSQLiteQuery: SimpleSQLiteQuery): DataSource.Factory<Int, Card>

    @Query(
        """
            UPDATE cards SET number_of_uses = number_of_uses+1
            WHERE card_id = :cardId
        """
    )
    suspend fun incrementNumberOfUses(cardId: String): Int

}