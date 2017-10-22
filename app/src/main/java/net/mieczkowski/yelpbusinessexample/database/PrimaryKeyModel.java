package net.mieczkowski.yelpbusinessexample.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class PrimaryKeyModel extends BaseModel implements Parcelable {

    @PrimaryKey(autoincrement = true)
    public long id;

    public PrimaryKeyModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
    }

    protected PrimaryKeyModel(Parcel in) {
        this.id = in.readLong();
    }

    public static final Creator<PrimaryKeyModel> CREATOR = new Creator<PrimaryKeyModel>() {
        @Override
        public PrimaryKeyModel createFromParcel(Parcel source) {
            return new PrimaryKeyModel(source);
        }

        @Override
        public PrimaryKeyModel[] newArray(int size) {
            return new PrimaryKeyModel[size];
        }
    };
}
