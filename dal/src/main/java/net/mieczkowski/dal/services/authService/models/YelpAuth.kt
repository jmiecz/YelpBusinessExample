package net.mieczkowski.dal.services.authService.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import net.mieczkowski.dal.cache.LocalDatabase

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
@Table(database = LocalDatabase::class)
class YelpAuth: BaseModel() {

    @PrimaryKey(autoincrement = true)
    var _id: Long = 0L

    @Column
    @JsonProperty("access_token")
    lateinit var accessToken: String

    @Column
    @JsonProperty("token_type")
    lateinit var tokenType: String
}