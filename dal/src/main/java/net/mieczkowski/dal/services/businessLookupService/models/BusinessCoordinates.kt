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
class BusinessCoordinates: PrimaryKey() {

    @Column
    @JsonProperty("latitude")
    var latitude: Double = 0.0

    @Column
    @JsonProperty("longitude")
    var longitude: Double = 0.0
}