package net.mieczkowski.yelpbusinessexample.services;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.mieczkowski.yelpbusinessexample.MyApplication;
import net.mieczkowski.yelpbusinessexample.models.business.BusinessDetails;
import net.mieczkowski.yelpbusinessexample.models.business.BusinessLookupRequest;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusinessWrapper;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness_Table;
import net.mieczkowski.yelpbusinessexample.networkInterfaces.IBusiness;
import net.mieczkowski.yelpbusinessexample.services.base.BaseService;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public class YelpBusinessLookupService extends BaseService<IBusiness> {

    public YelpBusinessLookupService() {
        super();
    }

    public YelpBusinessLookupService(IBusiness iDal) {
        super(iDal);
    }

    @Override
    protected Class<IBusiness> getInterfaceClass() {
        return IBusiness.class;
    }

    Single<ArrayList<YelpBusiness>> newLookUpByName(final BusinessLookupRequest businessLookupRequest) {
        return getDal().lookUpBusiness(
                businessLookupRequest.getName(),
                businessLookupRequest.getCity(),
                businessLookupRequest.getState(),
                businessLookupRequest.getCountry()
        )
                .map(new Function<YelpBusinessWrapper, ArrayList<YelpBusiness>>() {
                    @Override
                    public ArrayList<YelpBusiness> apply(@NonNull YelpBusinessWrapper yelpBusinessWrapper) throws Exception {
                        return yelpBusinessWrapper.getYelpBusinesses();
                    }
                })
                .toFlowable()
                .flatMap(new Function<ArrayList<YelpBusiness>, Publisher<YelpBusiness>>() {
                    @Override
                    public Publisher<YelpBusiness> apply(@NonNull ArrayList<YelpBusiness> yelpBusinesses) throws Exception {
                        return Flowable.fromIterable(yelpBusinesses);
                    }
                })
                .flatMap(new Function<YelpBusiness, Publisher<YelpBusiness>>() {
                    @Override
                    public Publisher<YelpBusiness> apply(@NonNull YelpBusiness yelpBusiness) throws Exception {
                        return getYelpBusinessWithDetails(yelpBusiness).toFlowable();
                    }
                })
                .toList().map(new Function<List<YelpBusiness>, ArrayList<YelpBusiness>>() {
                    @Override
                    public ArrayList<YelpBusiness> apply(@NonNull List<YelpBusiness> yelpBusinesses) throws Exception {
                        ArrayList<YelpBusiness> toReturn = new ArrayList<>();
                        toReturn.addAll(yelpBusinesses);

                        for (YelpBusiness yelpBusiness : toReturn) {
                            yelpBusiness.setSearchKey(businessLookupRequest.getSearchKey());
                        }

                        return toReturn;
                    }
                });
    }

    private Single<BusinessDetails> getBusinessDetails(YelpBusiness yelpBusiness) {
        return getDal().getBusinessDetails(yelpBusiness.getId());
    }

    private Single<YelpBusiness> getYelpBusinessWithDetails(YelpBusiness yelpBusiness) {
        return getBusinessDetails(yelpBusiness).zipWith(Single.just(yelpBusiness), new BiFunction<BusinessDetails, YelpBusiness, YelpBusiness>() {
            @Override
            public YelpBusiness apply(@NonNull BusinessDetails businessDetails, @NonNull YelpBusiness yelpBusiness) throws Exception {
                yelpBusiness.setBusinessDetails(businessDetails);
                return yelpBusiness;
            }
        });
    }

    public Single<ArrayList<YelpBusiness>> lookUpByName(BusinessLookupRequest businessLookupRequest) {

        if (!MyApplication.getServiceChecker().isConnected()) {
            ArrayList<YelpBusiness> yelpBusinesses = new ArrayList<>();
            yelpBusinesses.addAll(
                    SQLite.select()
                            .from(YelpBusiness.class)
                            .where(YelpBusiness_Table.searchKey.is(businessLookupRequest.getSearchKey()))
                            .queryList()
            );

            return Single.just(yelpBusinesses);
        }

        return newLookUpByName(businessLookupRequest)
                .doOnSuccess(new Consumer<ArrayList<YelpBusiness>>() {
                    @Override
                    public void accept(ArrayList<YelpBusiness> yelpBusinesses) throws Exception {
                        for (YelpBusiness yelpBusiness : yelpBusinesses) {
                            yelpBusiness.save();
                        }
                    }
                });
    }
}
