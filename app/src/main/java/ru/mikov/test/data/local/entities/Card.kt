package ru.mikov.test.data.local.entities

import androidx.room.*
import ru.mikov.test.data.local.ListConverter
import java.util.*

@Entity(tableName = "cards")
@TypeConverters(ListConverter::class)
data class Card(
    @PrimaryKey
    @ColumnInfo(name = "card_id")
    val cardId: String,
    val kind: String,
    @Embedded(prefix = "texture_")
    val texture: Texture,
    @Embedded(prefix = "loyalty_card_")
    val loyaltyCard: LoyaltyCard?,
    @Embedded(prefix = "certificate_")
    val certificate: Certificate?,
    @Embedded(prefix = "barcode_")
    val barcode: Barcode,
    @Embedded(prefix = "issuer_")
    val issuer: Issuer,
    @ColumnInfo(name = "number_of_uses")
    val numberOfUses: Int = 0,
    @ColumnInfo(name = "distance_to_nearest")
    val distanceToNearest:Int? = null
)

data class Barcode(
    val kind: String,
    val number: String
)

data class Issuer(
    val name: String,
    val categories: List<String> = emptyList()
)

data class LoyaltyCard(
    val balance: Int,
    val grade: String
)

data class Texture(
    val back: String,
    val front: String
)

data class Certificate(
    val value: Int,
    @ColumnInfo(name = "expire_date")
    val expireDate: Date
)


