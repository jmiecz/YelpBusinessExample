package net.mieczkowski.dal.cache.models

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.Table
import net.mieczkowski.dal.cache.LocalDatabase

//import com.raizlabs.android.dbflow.annotation.Column
//import com.raizlabs.android.dbflow.annotation.Table
//import com.raizlabs.android.dbflow.sql.language.SQLite

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
@Table(database = LocalDatabase::class)
class PreviousSearch: PrimaryKey() {

    companion object {

        private const val SEARCH_LIMIT = 10

//        fun getPreviousSearches(): List<PreviousSearch>{
//            return SQLite.select()
//                    .from(PreviousSearch::class.java)
//                    .orderBy(PreviousSearch_Table.id, false)
//                    .limit(SEARCH_LIMIT)
//                    .queryList();
//        }
    }

    @Column
    var searchTerm: String? = null

}