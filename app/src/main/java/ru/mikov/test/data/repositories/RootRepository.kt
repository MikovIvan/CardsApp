package ru.mikov.test.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.sqlite.db.SimpleSQLiteQuery
import ru.mikov.test.data.local.DbManager.db
import ru.mikov.test.data.local.entities.Card
import ru.mikov.test.data.local.entities.CardCategoryXRef
import ru.mikov.test.data.local.entities.Category
import ru.mikov.test.data.remote.NetworkManager
import ru.mikov.test.data.remote.res.CardsRes
import ru.mikov.test.extensions.data.toCard


object RootRepository {
    private val network = NetworkManager.api
    private val cardsDao = db.cardsDao()
    private val categoriesDao = db.categoriesDao()

    suspend fun isNeedUpdate(): Boolean {
        return cardsDao.getAllCards().isEmpty()
    }

    suspend fun sync() {
        getCards { cards ->
            cards.forEach { res ->
                cardsDao.insert(res.toCard())
            }

            val refs = cards.fold(mutableListOf<Pair<String, String>>()) { acc, res ->
                acc.also { list -> list.addAll(res.issuer.categories.map { res.number to it }) }
            }

            val categories = refs.map { it.second }
                .distinct()
                .map { Category(it) }

            categoriesDao.insert(categories)
            categoriesDao.insertRefs(refs.map { CardCategoryXRef(it.first, it.second) })
        }
    }

    private suspend fun getCards(result: (cards: List<CardsRes>) -> Unit) {
        result(network.getCards())
    }

    fun rawQueryCards(filter: CardFilter): DataSource.Factory<Int, Card> {
        return cardsDao.findCardsByRaw(SimpleSQLiteQuery(filter.toQuery()))
    }

    suspend fun incrementNumberOfUses(cardId: String) {
        cardsDao.incrementNumberOfUses(cardId)
    }

    fun findCategoriesData(): LiveData<List<String>> {
        return categoriesDao.findCategories()
    }
}

class CardFilter(
    val filter: String
) {
    fun toQuery(): String {
        val qb = QueryBuilder()
        qb.table("cards")
        when (filter) {
            "" -> qb.orderBy("number_of_uses", true)
            "A to Z" -> qb.orderBy("issuer_name", false)
            "Z to A" -> qb.orderBy("issuer_name", true)
        }

        if (filter != "A to Z" && filter != "Z to A" && filter != "") {
            qb.innerJoin("card_category_x_ref AS refs", "refs.a_id = card_id")
            qb.appendWhere("refs.t_id = '$filter'")
            qb.orderBy("number_of_uses", true)
        }

        return qb.build()
    }
}

class QueryBuilder() {
    private var table: String? = null
    private var selectColumns: String = "*"
    private var joinTables: String? = null
    private var whereCondition: String? = null
    private var order: String? = null

    fun build(): String {
        check(table != null) { "table must be not null" }
        val strBuilder = StringBuilder("SELECT ")
            .append("$selectColumns ")
            .append("FROM $table ")

        if (joinTables != null) strBuilder.append(joinTables)
        if (whereCondition != null) strBuilder.append(whereCondition)
        if (order != null) strBuilder.append(order)
        return strBuilder.toString()
    }

    fun table(table: String): QueryBuilder {
        this.table = table
        return this
    }

    fun orderBy(column: String, isDesc: Boolean = true): QueryBuilder {
        order = "ORDER BY $column ${if (isDesc) "DESC" else "ASC"}"
        return this
    }

    fun appendWhere(condition: String, logic: String = "AND"): QueryBuilder {
        if (whereCondition.isNullOrEmpty()) whereCondition = "WHERE $condition "
        else whereCondition += "$logic $condition "
        return this
    }

    fun innerJoin(table: String, on: String): QueryBuilder {
        if (joinTables.isNullOrEmpty()) joinTables = "INNER JOIN $table ON $on "
        else joinTables += "INNER JOIN $table ON $on"
        return this
    }
}