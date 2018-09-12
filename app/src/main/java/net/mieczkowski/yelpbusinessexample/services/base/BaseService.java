package net.mieczkowski.yelpbusinessexample.services.base;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public abstract class BaseService<I> {

    private I iDal;

    public BaseService(String url) {
        iDal = new RetroService<>(getInterfaceClass(), url).getRetroInterface();
    }

    public BaseService() {
        iDal = new RetroService<>(getInterfaceClass(), YelpInfo.INSTANCE.getAPI_URL()).getRetroInterface();
    }

    public BaseService(I iDal) {
        this.iDal = iDal;
    }

    protected abstract Class<I> getInterfaceClass();

    protected I getDal() {
        return iDal;
    }
}
