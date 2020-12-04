package ru.mikov.test.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.mikov.test.data.local.entities.CardCategoryXRef
import ru.mikov.test.data.local.entities.Category

@Dao
interface CategoriesDao : BaseDao<Category> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRefs(refs: List<CardCategoryXRef>): List<Long>

    @Query(
        """
        SELECT category FROM category
    """
    )
    fun findCategories(): LiveData<List<String>>
}