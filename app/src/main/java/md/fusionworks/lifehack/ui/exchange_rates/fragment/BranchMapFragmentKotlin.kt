package md.fusionworks.lifehack.ui.exchange_rates.fragment

import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.badoo.mobile.util.WeakHandler
import com.directions.route.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_branch_map_k.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.ui.base.view.BaseFragment
import md.fusionworks.lifehack.ui.exchange_rates.event.ScrollToMapEvent
import md.fusionworks.lifehack.ui.exchange_rates.event.ShowBranchMapInfoWindowEvent
import md.fusionworks.lifehack.ui.exchange_rates.event.ShowRouteOnMapEvent
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.DialogUtil
import md.fusionworks.lifehack.util.MapUtil
import md.fusionworks.lifehack.util.rx.RxBusKotlin
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BranchMapFragmentKotlin : BaseFragment() {

  private lateinit var map: GoogleMap
  private val branchMap = HashMap<Marker, BranchModel>()
  private lateinit var googleApiClient: GoogleApiClient
  private var myLastLocation: Location? = null
  private lateinit var routePolylines: ArrayList<Polyline>
  private lateinit var branchModelList: List<BranchModel>
  private lateinit var weakHandler: WeakHandler

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    branchModelList = arguments.getParcelableArrayList<BranchModel>(
        Constant.EXTRA_PARAM_BRANCH_LIST)
    routePolylines = arrayListOf<Polyline>()
    weakHandler = WeakHandler()
    buildGoogleApiClient()
  }

  override fun onStart() {
    super.onStart()
    googleApiClient.connect()
  }

  override fun onStop() {
    super.onStop()
    if (googleApiClient.isConnected) {
      googleApiClient.disconnect()
    }
  }

  override fun onResume() {
    super.onResume()
    branchMapView.onResume()
  }

  override fun onDestroy() {
    weakHandler.removeCallbacksAndMessages(null)
    if (map != null) {
      map.isMyLocationEnabled = false
      map.clear()
    }
    branchMapView.onDestroy()
    super.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    branchMapView.onLowMemory()
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val view = inflater?.inflate(R.layout.fragment_branch_map_k, container, false)
    initializeMap(savedInstanceState)
    return view
  }

  override fun listenForEvents() {
    super.listenForEvents()
    RxBusKotlin.event(ShowRouteOnMapEvent::class.java).compose(
        this.bindToLifecycle<ShowRouteOnMapEvent>()).subscribe { showRouteOnMapEvent ->
      showRouteOnMap(showRouteOnMapEvent.branchModel)
    }

    RxBusKotlin.event(ShowBranchMapInfoWindowEvent::class.java).compose(
        this.bindToLifecycle<ShowBranchMapInfoWindowEvent>()).subscribe { showBranchMapInfoWindowEvent ->
      smoothShowInfoWindow(showBranchMapInfoWindowEvent.branchModel)
    }
  }

  private fun initializeMap(savedInstanceState: Bundle?) {
    branchMapView.onCreate(savedInstanceState)
    enableMapGesturesInScrollView()
    branchMapView.getMapAsync { googleMap ->
      map = googleMap
      map.uiSettings.isMyLocationButtonEnabled = true
      map.uiSettings.isZoomControlsEnabled = true
      map.uiSettings.isCompassEnabled = true
      map.isMyLocationEnabled = true
      MapsInitializer.initialize(activity)
      populateBranchesMap(branchModelList)
      weakHandler.postDelayed({ RxBusKotlin.postIfHasObservers(ScrollToMapEvent()) }, 500)
      map.setOnMarkerClickListener { marker ->
        smoothShowInfoWindow(branchMap[marker])
        true
      }
    }
  }

  private fun enableMapGesturesInScrollView() {
    imageOverMap.setOnTouchListener { v, event ->
      val action = event.action
      when (action) {
        MotionEvent.ACTION_DOWN -> {
          rootLayout.requestDisallowInterceptTouchEvent(true)
          false
        }
        MotionEvent.ACTION_UP -> {
          rootLayout.requestDisallowInterceptTouchEvent(false)
          true
        }
        MotionEvent.ACTION_MOVE -> {
          rootLayout.requestDisallowInterceptTouchEvent(true)
          false
        }
        else -> true
      }
    }
  }

  private fun pinHomeMarker(location: Location?) {
    if (location != null) {
      MapUtil.createMarker(map, location.latitude, location.longitude,
          R.drawable.home_pin_icon)
      MapUtil.goToPosition(map, location.latitude, location.longitude, false,
          Constant.CAMERA_ZOOM)
    }
  }

  private fun populateBranchesMap(branchList: List<BranchModel>?) {
    map.clear()
    pinHomeMarker(myLastLocation)
    if (branchList != null) for (branch in branchList) {
      val marker = MapUtil.createMarker(map, branch.address.location.lat,
          branch.address.location.lng, R.drawable.exchange_pin_icon)
      branchMap.put(marker, branch)
    }
  }

  private fun smoothShowInfoWindow(branch: BranchModel?) {
    weakHandler.postDelayed({ DialogUtil.showBranchMapInfoWindow(activity, branch) }, 100)
    weakHandler.postDelayed(
        {
          MapUtil.goToPosition(map, branch?.address?.location?.lat,
              branch?.address?.location?.lng, false)
        }, 200)
  }

  @Synchronized protected fun buildGoogleApiClient() {
    googleApiClient = GoogleApiClient.Builder(activity).addConnectionCallbacks(
        object : GoogleApiClient.ConnectionCallbacks {
          override fun onConnected(bundle: Bundle?) {
            myLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
            pinHomeMarker(myLastLocation)
          }

          override fun onConnectionSuspended(i: Int) {
            googleApiClient.connect()
          }
        }).addOnConnectionFailedListener { connectionResult ->

    }.addApi(LocationServices.API).build()
  }

  private fun showRouteOnMap(branch: BranchModel?) {
    if (myLastLocation != null) {
      showLoadingDialog()
      val routing = Routing.Builder().travelMode(AbstractRouting.TravelMode.DRIVING).withListener(
          object : RoutingListener {
            override fun onRoutingFailure(e: RouteException) {
              hideLoadingDialog()
              showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                  getString(R.string.error_something_gone_wrong))
            }

            override fun onRoutingStart() {

            }

            override fun onRoutingSuccess(arrayList: ArrayList<Route>, j: Int) {
              hideLoadingDialog()
              MapUtil.goToPosition(map, branch?.address?.location?.lat,
                  branch?.address?.location?.lng, false)

              if (routePolylines.size > 0) {
                for (poly in routePolylines) {
                  poly.remove()
                }
              }
              routePolylines = ArrayList<Polyline>(arrayList.size)

              for (i in arrayList.indices) {
                val polyOptions = PolylineOptions()
                polyOptions.color(ContextCompat.getColor(activity, R.color.mainColorBlue))
                polyOptions.width((10 + i * 3).toFloat())
                polyOptions.addAll(arrayList[i].points)
                val polyline = map.addPolyline(polyOptions)
                routePolylines.add(polyline)
              }
            }

            override fun onRoutingCancelled() {

            }
          }).alternativeRoutes(false).waypoints(
          LatLng(myLastLocation!!.latitude, myLastLocation!!.longitude),
          LatLng(branch?.address?.location?.lat ?: 0.toDouble(),
              branch?.address?.location?.lng ?: 0.toDouble())).build()
      routing.execute()
    } else {
      showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
          getString(R.string.error_something_gone_wrong))
    }
  }

  companion object {

    fun newInstance(branchModelList: List<BranchModel>?): BranchMapFragmentKotlin {
      val branchMapFragment = BranchMapFragmentKotlin()
      val bundle = Bundle()
      bundle.putParcelableArrayList(Constant.EXTRA_PARAM_BRANCH_LIST,
          ArrayList(branchModelList))
      branchMapFragment.arguments = bundle
      return branchMapFragment
    }
  }
}
