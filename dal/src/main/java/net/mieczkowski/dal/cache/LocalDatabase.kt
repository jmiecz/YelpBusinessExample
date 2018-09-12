package net.mieczkowski.dal.cache

import com.raizlabs.android.dbflow.annotation.Database
import net.mieczkowski.dal.cache.LocalDatabase.NAME
import net.mieczkowski.dal.cache.LocalDatabase.VERSION

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
@Database(name = NAME, version = VERSION)
object LocalDatabase {

    const val NAME = "LocalDatabase"
    const val VERSION = 1
}