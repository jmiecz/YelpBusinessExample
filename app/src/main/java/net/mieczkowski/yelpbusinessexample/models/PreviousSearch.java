package net.mieczkowski.yelpbusinessexample.models;

import android.os.Parcel;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.mieczkowski.yelpbusinessexample.database.MyDatabase;
import net.mieczkowski.yelpbusinessexample.database.PrimaryKeyModel;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness_Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

@Table(database = MyDatabase.class)
public class PreviousSearch extends PrimaryKeyModel {

    private static final int SEARCH_LIMIT = 10;

    public static List<PreviousSearch> getPreviousSearches(){
        return SQLite.select()
                .from(PreviousSearch.class)
                .limit(SEARCH_LIMIT)
                .queryList();
    }

    @Column
    String searchTerm;

    public PreviousSearch(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.searchTerm);
    }

    protected PreviousSearch(Parcel in) {
        super(in);
        this.searchTerm = in.readString();
    }

    public static final Creator<PreviousSearch> CREATOR = new Creator<PreviousSearch>() {
        @Override
        public PreviousSearch createFromParcel(Parcel source) {
            return new PreviousSearch(source);
        }

        @Override
        public PreviousSearch[] newArray(int size) {
            return new PreviousSearch[size];
        }
    };
}
