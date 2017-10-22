package net.mieczkowski.yelpbusinessexample.models;

import android.os.Parcel;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.mieczkowski.yelpbusinessexample.database.MyDatabase;
import net.mieczkowski.yelpbusinessexample.database.PrimaryKeyModel;

import java.util.List;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

@Table(database = MyDatabase.class)
public class PreviousSearch extends PrimaryKeyModel {

    private static final int SEARCH_LIMIT = 10;

    public static List<PreviousSearch> getPreviousSearches() {
        return SQLite.select()
                .from(PreviousSearch.class)
                .orderBy(PreviousSearch_Table.id, false)
                .limit(SEARCH_LIMIT)
                .queryList();
    }

    @Column
    String searchTerm;

    public PreviousSearch() {
    }

    public PreviousSearch(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

}
