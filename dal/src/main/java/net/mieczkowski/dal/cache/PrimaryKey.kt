package net.mieczkowski.dal.cache

import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.structure.BaseModel

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
open class PrimaryKey: BaseModel {

    @PrimaryKey(autoincrement = true)
    var _id: Long = 0L
}