package net.mieczkowski.dal.cache.models

import com.raizlabs.android.dbflow.annotation.PrimaryKey
//import com.raizlabs.android.dbflow.structure.BaseModel

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */

// : BaseModel
open class PrimaryKey {

    @PrimaryKey(autoincrement = true)
    var _id: Long = 0L
}