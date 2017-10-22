package net.mieczkowski.yelpbusinessexample.models.business;

import android.os.Parcel;
import android.os.Parcelable;

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
public class BusinessDetails extends BaseModel implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.imgUrl);
        dest.writeByte(this.isClaimed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isClosed ? (byte) 1 : (byte) 0);
        dest.writeString(this.businessUrl);
        dest.writeString(this.priceRating);
        dest.writeDouble(this.rating);
        dest.writeInt(this.reviewCount);
        dest.writeStringList(this.businessPhotos);
    }

    protected BusinessDetails(Parcel in) {
        this.id = in.readString();
        this.imgUrl = in.readString();
        this.isClaimed = in.readByte() != 0;
        this.isClosed = in.readByte() != 0;
        this.businessUrl = in.readString();
        this.priceRating = in.readString();
        this.rating = in.readDouble();
        this.reviewCount = in.readInt();
        this.businessPhotos = in.createStringArrayList();
    }

    public static final Creator<BusinessDetails> CREATOR = new Creator<BusinessDetails>() {
        @Override
        public BusinessDetails createFromParcel(Parcel source) {
            return new BusinessDetails(source);
        }

        @Override
        public BusinessDetails[] newArray(int size) {
            return new BusinessDetails[size];
        }
    };
}
