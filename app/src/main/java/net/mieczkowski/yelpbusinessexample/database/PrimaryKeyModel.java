package net.mieczkowski.yelpbusinessexample.database;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class PrimaryKeyModel extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    public PrimaryKeyModel() {
    }

}
