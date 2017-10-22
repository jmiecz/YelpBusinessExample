package net.mieczkowski.yelpbusinessexample.controllers.base;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;

import net.mieczkowski.yelpbusinessexample.interfaces.IToolbar;

/**
 * Created by Josh Mieczkowski on 10/19/2017.
 */

public abstract class BaseController extends ButterKnifeController {

    public void show(Controller controller) {
        show(controller, new HorizontalChangeHandler());
    }

    public void show(Controller controller, ControllerChangeHandler controllerChangeHandler) {
        show(controller.getRouter(), controllerChangeHandler);
    }

    public void show(Router router) {
        show(router, new HorizontalChangeHandler());
    }

    public void show(Router router, ControllerChangeHandler controllerChangeHandler) {
        if (!router.popToTag(getClass().getName())) {
            router.pushController(RouterTransaction.with(this)
                    .popChangeHandler(controllerChangeHandler)
                    .pushChangeHandler(controllerChangeHandler)
                    .tag(getClass().getName()));
        }
    }

    protected IToolbar getIToolbar() {
        if (!(getActivity() instanceof IToolbar)) {
            throw new RuntimeException("Need to implement IToolbar");
        }

        return (IToolbar) getActivity();
    }

    public void setTitle(String title) {
        getIToolbar().getSupportActionBar().setTitle(title);
    }

}
