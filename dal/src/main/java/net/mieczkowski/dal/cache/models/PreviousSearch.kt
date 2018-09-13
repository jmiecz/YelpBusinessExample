package net.mieczkowski.dal.cache.models

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel
import net.mieczkowski.dal.cache.LocalDatabase

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
@Table(database = LocalDatabase::class)
class PreviousSearch : BaseModel(){

    companion object {

        private const val SEARCH_LIMIT = 10

        fun getPreviousSearches(): List<PreviousSearch> {
            return SQLite.select()
                    .from(PreviousSearch::class.java)
                    .orderBy(PreviousSearch_Table._id, false)
                    .limit(SEARCH_LIMIT)
                    .queryList()
        }
    }

    @PrimaryKey(autoincrement = true)
    var _id: Long = 0L

    @Column
    lateinit var searchTerm: String

}