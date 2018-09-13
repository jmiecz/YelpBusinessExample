package net.mieczkowski.dal.services.businessLookupService

import com.raizlabs.android.dbflow.config.FlowManager
import net.mieczkowski.dal.mockedData.MockedBusinessLookupData
import net.mieczkowski.dal.services.businessLookupService.models.BusinessLookupRequest
import net.mieczkowski.dal.services.businessLookupService.models.MyLocation
import net.mieczkowski.dal.tools.ServiceChecker
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.ArrayList

/**
 * Created by Josh Mieczkowski on 9/12/2018.
 */
@RunWith(RobolectricTestRunner::class)
class BusinessLookupServiceTest : KoinTest {

    var mockServiceChecker = mock(ServiceChecker::class.java)

    val businessLookupService: BusinessLookupService by inject()

    @Test
    fun `testing new lookup by name`() {
        FlowManager.init(RuntimeEnvironment.application)

        startKoin(listOf(module {
            single { MockedBusinessLookupData() as BusinessContract }
            factory { BusinessLookupService(get(), mockServiceChecker) }
        }))

        `when`(mockServiceChecker.hasInternetAccess()).thenReturn(true)

        val testObs = businessLookupService.lookUpByName(BusinessLookupRequest("TEST", MyLocation(0.0, 0.0)))
                .test()

        testObs.awaitTerminalEvent()

        testObs.assertNoErrors()
                .assertValue {
                    assertEquals(2, it.size)

                    val yelpBusiness = it[0]
                    val businessLocation = yelpBusiness.businessLocation!!
                    val businessCoordinates = yelpBusiness.businessCoordinates!!
                    val businessDetails = yelpBusiness.businessDetails!!

                    val photos = listOf(
                            "http://s3-media3.fl.yelpcdn.com/bphoto/--8oiPVp0AsjoWHqaY1rDQ/o.jpg",
                            "http://s3-media2.fl.yelpcdn.com/bphoto/ybXbObsm7QGw3SGPA1_WXA/o.jpg",
                            "http://s3-media3.fl.yelpcdn.com/bphoto/7rZ061Wm4tRZ-iwAhkRSFA/o.jpg"
                    )

                    assertEquals("Gary Danko", yelpBusiness.name)
                    assertEquals("+14157492060", yelpBusiness.phone)
                    assertEquals("gary-danko-san-francisco", yelpBusiness.id)
                    assertEquals("TEST", yelpBusiness.searchKey)

                    assertEquals("800 N Point St", businessLocation.address1)
                    assertEquals("", businessLocation.address2)
                    assertEquals("", businessLocation.address3)
                    assertEquals("San Francisco", businessLocation.city)
                    assertEquals("CA", businessLocation.state)
                    assertEquals("94109", businessLocation.postalCode)
                    assertEquals("US", businessLocation.country)

                    assertEquals(37.80587, businessCoordinates.latitude, .00001)
                    assertEquals(-122.42058, businessCoordinates.longitude, .00001)

                    assertEquals(yelpBusiness.id, businessDetails.id)
                    assertEquals("https://s3-media4.fl.yelpcdn.com/bphoto/--8oiPVp0AsjoWHqaY1rDQ/o.jpg", businessDetails.imgUrl)
                    assertEquals(false, businessDetails.isClaimed)
                    assertEquals(false, businessDetails.isClosed)
                    assertEquals("https://www.yelp.com/biz/gary-danko-san-francisco", businessDetails.businessUrl)
                    assertEquals("\$\$\$\$", businessDetails.priceRating)
                    assertEquals(4.5, businessDetails.rating, .1)
                    assertEquals(4521, businessDetails.reviewCount)
                    assertEquals(photos, businessDetails.businessPhotos)

                    true
                }

        stopKoin()
    }
}