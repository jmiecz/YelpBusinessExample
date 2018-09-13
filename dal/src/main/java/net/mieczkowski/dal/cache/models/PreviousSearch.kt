package net.mieczkowski.dal.cache.models

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel
import net.mieczkowski.dal.cache.LocalDatabase
import com.raizlabs.android.dbflow.annotation.ConflictAction
import java.util.*


/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
@Table(database = LocalDatabase::class, updateConflict = ConflictAction.REPLACE, insertConflict = ConflictAction.REPLACE)
class PreviousSearch : BaseModel(){

    companion object {

        private const val SEARCH_LIMIT = 10

        fun getPreviousSearches(): List<PreviousSearch> {
            return SQLite.select()
                    .from(PreviousSearch::class.java)
                    .orderBy(PreviousSearch_Table.timeStamp, false)
                    .limit(SEARCH_LIMIT)
                    .queryList()
        }
    }

    @PrimaryKey
    @Column
    lateinit var searchTerm: String

    @Column
    var timeStamp = Date().time

}