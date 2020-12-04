package ru.mikov.test.data.local.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey
    val category: String
)

@Entity(
    tableName = "card_category_x_ref",
    primaryKeys = ["t_id", "a_id"],
    foreignKeys = [
        ForeignKey(
            entity = Card::class,
            parentColumns = ["card_id"],
            childColumns = ["a_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class CardCategoryXRef(
    @ColumnInfo(name = "a_id")
    val cardId: String,
    @ColumnInfo(name = "t_id")
    val tagId: String
)