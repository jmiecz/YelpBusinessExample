package net.mieczkowski.yelpbusinessexample.models.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.mieczkowski.yelpbusinessexample.database.MyDatabase;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

@Table(database = MyDatabase.class)
public class YelpBusiness extends BaseModel{

    @PrimaryKey
    @JsonProperty("id")
    public String id;

    @Column
    @JsonProperty("name")
    String name;

    @Column
    @JsonProperty("phone")
    String phone;

    @ForeignKey(saveForeignKeyModel = true)
    @JsonProperty("location")
    BusinessLocation businessLocation;

    @ForeignKey(saveForeignKeyModel = true)
    @JsonProperty("coordinates")
    BusinessCoordinates businessCoordinates;

    @ForeignKey(saveForeignKeyModel = true)
    @JsonIgnore
    BusinessDetails businessDetails;

    @Column
    @JsonIgnore
    String searchKey;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public BusinessLocation getBusinessLocation() {
        return businessLocation;
    }

    public BusinessCoordinates getBusinessCoordinates() {
        return businessCoordinates;
    }

    public BusinessDetails getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(BusinessDetails businessDetails) {
        this.businessDetails = businessDetails;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public YelpBusiness() {
    }

}
