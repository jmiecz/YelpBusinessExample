package net.mieczkowski.yelpbusinessexample.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "MyDataBase";

    public static final int VERSION = 3;
}
