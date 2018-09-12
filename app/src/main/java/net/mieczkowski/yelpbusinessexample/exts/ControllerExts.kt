package net.mieczkowski.yelpbusinessexample.exts

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */

fun Controller.show(controller: Controller,
                    pushChangeHandler: ControllerChangeHandler = HorizontalChangeHandler(),
                    popChangeHandler: ControllerChangeHandler = HorizontalChangeHandler()) {
    show(controller.router, pushChangeHandler, popChangeHandler)
}

fun Controller.show(router: Router,
                    pushChangeHandler: ControllerChangeHandler = HorizontalChangeHandler(),
                    popChangeHandler: ControllerChangeHandler = HorizontalChangeHandler()) {
    router.pushController(
            RouterTransaction.with(this)
                    .pushChangeHandler(pushChangeHandler)
                    .popChangeHandler(popChangeHandler)
    )
}

fun Controller.setRoot(router: Router) {
    router.setRoot(RouterTransaction.with(this))
}

fun Controller.setRoot(router: Router,
                       pushChangeHandler: ControllerChangeHandler = HorizontalChangeHandler(),
                       popChangeHandler: ControllerChangeHandler = HorizontalChangeHandler()) {
    router.setRoot(
            RouterTransaction.with(this)
                    .pushChangeHandler(pushChangeHandler)
                    .popChangeHandler(popChangeHandler)
    )
}