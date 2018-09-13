package net.mieczkowski.dal.services.businessLookupService.models

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
class BusinessLocation : BaseModel() {

    @PrimaryKey(autoincrement = true)
    var _id: Long = 0L

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