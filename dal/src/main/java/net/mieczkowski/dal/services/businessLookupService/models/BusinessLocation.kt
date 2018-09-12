package net.mieczkowski.dal.services.businessLookupService.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.Table
import net.mieczkowski.dal.cache.LocalDatabase
import net.mieczkowski.dal.cache.models.PrimaryKey

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
@Table(database = LocalDatabase::class)
class BusinessLocation: PrimaryKey() {

    @Column
    @JsonProperty("address1")
    var address1: String? = null

    @Column
    @JsonProperty("address2")
    var address2: String? = null

    @Column
    @JsonProperty("address3")
    var address3: String? = null

    @Column
    @JsonProperty("city")
    var city: String? = null

    @Column
    @JsonProperty("state")
    var state: String? = null

    @Column
    @JsonProperty("postal_code")
    var postalCode: String? = null

    @Column
    @JsonProperty("country")
    var country: String? = null
}