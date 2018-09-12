package net.mieczkowski.yelpbusinessexample.services;

import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.mieczkowski.yelpbusinessexample.interfaces.network.IAuth;
import net.mieczkowski.yelpbusinessexample.models.auth.YelpAuthRequest;
import net.mieczkowski.yelpbusinessexample.services.base.BaseService;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class YelpAuthService extends BaseService<IAuth> {

    public YelpAuthService() {
        super();
    }

    public YelpAuthService(IAuth iDal) {
        super(iDal);
    }

    @Override
    protected Class<IAuth> getInterfaceClass() {
        return IAuth.class;
    }

    Single<YelpAuth> getNewYelpAuth() {
        YelpAuthRequest yelpAuthRequest = new YelpAuthRequest();
        return getDal().getYelpAuth(yelpAuthRequest.getClientID(), yelpAuthRequest.getClientSecret(), yelpAuthRequest.getGrantType());
    }

    @Nullable
    public YelpAuth getCacheYelpAuth() {
        return SQLite.select()
                .from(YelpAuth.class)
                .querySingle();
    }

    public Single<YelpAuth> getYelpAuth() {
        YelpAuth yelpAuth = getCacheYelpAuth();

        if (yelpAuth != null) {
            return Single.just(yelpAuth);
        }

        return getNewYelpAuth()
                .doOnSuccess(new Consumer<YelpAuth>() {
                    @Override
                    public void accept(YelpAuth yelpAuth) throws Exception {
                        clearCache();

                        yelpAuth.save();
                    }
                });
    }

    public void clearCache() {
        Delete.table(YelpAuth.class);
    }
}
