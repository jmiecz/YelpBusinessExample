package net.mieczkowski.yelpbusinessexample.services;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.mieczkowski.yelpbusinessexample.MyApplication;
import net.mieczkowski.yelpbusinessexample.interfaces.network.IBusiness;
import net.mieczkowski.yelpbusinessexample.models.business.BusinessDetails;
import net.mieczkowski.yelpbusinessexample.models.business.BusinessLookupRequest;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusinessWrapper;
import net.mieczkowski.yelpbusinessexample.models.business.YelpBusiness_Table;
import net.mieczkowski.yelpbusinessexample.services.base.BaseService;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
                businessLookupRequest.getSearchTerm(),
                businessLookupRequest.getLocation().getLatitude(),
                businessLookupRequest.getLocation().getLongitude()
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
                            yelpBusiness.setSearchKey(businessLookupRequest.getSearchTerm());
                        }

                        return toReturn;
                    }
                }).map(sortFunction);
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

    private Function<ArrayList<YelpBusiness>, ArrayList<YelpBusiness>> sortFunction = new Function<ArrayList<YelpBusiness>, ArrayList<YelpBusiness>>() {
        @Override
        public ArrayList<YelpBusiness> apply(@NonNull ArrayList<YelpBusiness> yelpBusinesses) throws Exception {
            Collections.sort(yelpBusinesses, new Comparator<YelpBusiness>() {
                @Override
                public int compare(YelpBusiness yelpBusiness, YelpBusiness yelpBusiness2) {
                    int reviewCount1 = yelpBusiness.getBusinessDetails().getReviewCount();
                    int reviewCount2 = yelpBusiness2.getBusinessDetails().getReviewCount();

                    if(reviewCount1 > reviewCount2){
                        return -1;
                    }else if(reviewCount1 < reviewCount2){
                        return 1;
                    }

                    return 0;
                }
            });

            Collections.sort(yelpBusinesses, new Comparator<YelpBusiness>() {
                @Override
                public int compare(YelpBusiness yelpBusiness, YelpBusiness yelpBusiness2) {
                    double rating1 = yelpBusiness.getBusinessDetails().getRating();
                    double rating2 = yelpBusiness2.getBusinessDetails().getRating();

                    if(rating1 > rating2){
                        return -1;
                    }else if(rating1 < rating2){
                        return 1;
                    }

                    return 0;
                }
            });
            return yelpBusinesses;
        }
    };

    public Single<ArrayList<YelpBusiness>> lookUpByNameCache(String searchTerm){
        ArrayList<YelpBusiness> yelpBusinesses = new ArrayList<>();
        yelpBusinesses.addAll(
                SQLite.select()
                        .from(YelpBusiness.class)
                        .where(YelpBusiness_Table.searchKey.is(searchTerm))
                        .queryList()
        );

        return Single.just(yelpBusinesses).map(sortFunction);
    }

    public Single<ArrayList<YelpBusiness>> lookUpByName(BusinessLookupRequest businessLookupRequest) {

        if (!MyApplication.getServiceChecker().isConnected()) {
            lookUpByNameCache(businessLookupRequest.getSearchTerm());
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
