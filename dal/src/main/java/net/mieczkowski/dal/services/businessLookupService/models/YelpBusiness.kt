package net.mieczkowski.dal.services.businessLookupService.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import net.mieczkowski.dal.cache.LocalDatabase


/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */

@Table(database = LocalDatabase::class)
class YelpBusiness: BaseModel() {

    @PrimaryKey
    @JsonProperty("id")
    lateinit var id: String

    @Column
    @JsonProperty("name")
    var name: String? = null

    @Column
    @JsonProperty("phone")
    var phone: String? = null

    @ForeignKey(saveForeignKeyModel = true)
    @JsonProperty("location")
    var businessLocation: BusinessLocation? = null

    @ForeignKey(saveForeignKeyModel = true)
    @JsonProperty("coordinates")
    var businessCoordinates: BusinessCoordinates? = null

    @ForeignKey(saveForeignKeyModel = true)
    @JsonIgnore
    var businessDetails: BusinessDetails? = null

    @Column
    @JsonIgnore
    var searchKey: String? = null
}