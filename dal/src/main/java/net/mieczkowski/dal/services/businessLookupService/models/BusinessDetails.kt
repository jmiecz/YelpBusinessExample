package net.mieczkowski.dal.services.businessLookupService.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import net.mieczkowski.dal.cache.LocalDatabase
import net.mieczkowski.dal.exts.convertJsonToList

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */
@Table(database = LocalDatabase::class)
class BusinessDetails : BaseModel() {

    @PrimaryKey
    @JsonProperty("id")
    lateinit var id: String

    @Column
    @JsonProperty("image_url")
    var imgUrl: String? = null

    @Column
    @JsonProperty("is_claimed")
    var isClaimed: Boolean = false

    @Column
    @JsonProperty("is_closed")
    var isClosed: Boolean = false

    @Column
    @JsonProperty("url")
    var businessUrl: String? = null

    @Column
    @JsonProperty("price")
    var priceRating: String? = null

    @Column
    @JsonProperty("rating")
    var rating: Double = 0.0

    @Column
    @JsonProperty("review_count")
    var reviewCount: Int = 0

    @Column
    @JsonIgnore
    var businessPhotosString: String? = null

    @JsonIgnore
    var businessPhotos = listOf<String>()
        get() {
            if (field.isEmpty() && businessPhotosString?.isNotEmpty() == true) {
                field = businessPhotosString!!.convertJsonToList()
            }

            return field
        }

    @JsonProperty("photos")
    fun setPhotos(photos: JsonNode) {
        this.businessPhotosString = photos.toString()
    }
}