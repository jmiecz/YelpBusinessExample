package net.mieczkowski.yelpbusinessexample.models.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.mieczkowski.yelpbusinessexample.database.MyDatabase;
import net.mieczkowski.yelpbusinessexample.services.base.RetroService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

@Table(database = MyDatabase.class)
public class BusinessDetails extends BaseModel {

    @Column
    @PrimaryKey
    @JsonProperty("id")
    public String id;

    @Column
    @JsonProperty("image_url")
    String imgUrl;

    @Column
    @JsonProperty("is_claimed")
    boolean isClaimed;

    @Column
    @JsonProperty("is_closed")
    boolean isClosed;

    @Column
    @JsonProperty("url")
    String businessUrl;

    @Column
    @JsonProperty("price")
    String priceRating;

    @Column
    @JsonProperty("rating")
    double rating;

    @Column
    @JsonProperty("review_count")
    int reviewCount;

    @Column
    @JsonIgnore
    String businessPhotosString;

    @JsonIgnore
    ArrayList<String> businessPhotos = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public boolean isClaimed() {
        return isClaimed;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public String getBusinessUrl() {
        return businessUrl;
    }

    public String getPriceRating() {
        return priceRating;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    @JsonProperty("photos")
    public void setPhotos(JsonNode photos) {
        this.businessPhotosString = photos.toString();
    }

    public ArrayList<String> getBusinessPhotos() {
        if (businessPhotos.isEmpty()) {
            if (businessPhotosString != null && !businessPhotosString.isEmpty()) {

                try {
                    businessPhotos = RetroService.getArrayList(businessPhotosString, String.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return businessPhotos;
    }


    public BusinessDetails() {
    }
}
