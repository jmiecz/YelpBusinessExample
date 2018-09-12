package net.mieczkowski.dal.services.authService.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.Table
import net.mieczkowski.dal.cache.LocalDatabase
import net.mieczkowski.dal.cache.PrimaryKey

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
@Table(database = LocalDatabase::class)
class YelpAuth: PrimaryKey() {

    @Column
    @JsonProperty("access_token")
    lateinit var accessToken: String

    @Column
    @JsonProperty("token_type")
    lateinit var tokenType: String
}