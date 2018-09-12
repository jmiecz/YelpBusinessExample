package net.mieczkowski.dal.cache

import com.raizlabs.android.dbflow.annotation.Database
import net.mieczkowski.dal.cache.LocalDatabase.Companion.NAME
import net.mieczkowski.dal.cache.LocalDatabase.Companion.VERSION

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
@Database(name = NAME, version = VERSION)
class LocalDatabase {

    companion object {
        const val NAME = "LocalDatabase"
        const val VERSION = 1
    }
}