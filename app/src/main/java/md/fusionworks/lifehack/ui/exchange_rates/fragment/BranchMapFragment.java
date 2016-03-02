package md.fusionworks.lifehack.ui.exchange_rates.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.Bind;
import com.badoo.mobile.util.WeakHandler;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.exchange_rates.event.ScrollToMapEvent;
import md.fusionworks.lifehack.ui.exchange_rates.event.ShowBranchMapInfoWindowEvent;
import md.fusionworks.lifehack.ui.exchange_rates.event.ShowRouteOnMapEvent;
import md.fusionworks.lifehack.ui.base.view.BaseFragment;
import md.fusionworks.lifehack.ui.exchange_rates.model.BranchModel;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.DialogUtil;
import md.fusionworks.lifehack.util.MapUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class BranchMapFragment extends BaseFragment {

  @Bind(R.id.rootLayout) FrameLayout rootLayout;
  @Bind(R.id.branchMap) MapView mapView;
  @Bind(R.id.imageOverMap) ImageView imageOverMap;

  private GoogleMap map;
  private Map<Marker, BranchModel> branchMap = new HashMap<>();
  private GoogleApiClient googleApiClient;
  private Location myLastLocation;
  private ArrayList<Polyline> routePolylines;
  private List<BranchModel> branchModelList;
  private WeakHandler weakHandler;

  public BranchMapFragment() {
  }

  public static BranchMapFragment newInstance(List<BranchModel> branchModelList) {
    BranchMapFragment branchMapFragment = new BranchMapFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList(Constant.EXTRA_PARAM_BRANCH_LIST,
        new ArrayList<>(branchModelList));
    branchMapFragment.setArguments(bundle);
    return branchMapFragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    branchModelList = getArguments().getParcelableArrayList(Constant.EXTRA_PARAM_BRANCH_LIST);
    routePolylines = new ArrayList<>();
    weakHandler = new WeakHandler();
    buildGoogleApiClient();
  }

  @Override public void onStart() {
    super.onStart();
    googleApiClient.connect();
  }

  @Override public void onStop() {
    super.onStop();
    if (googleApiClient.isConnected()) {
      googleApiClient.disconnect();
    }
  }

  @Override public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override public void onDestroy() {
    weakHandler.removeCallbacksAndMessages(null);
    if (map != null) {
      map.setMyLocationEnabled(false);
      map.clear();
    }
    mapView.onDestroy();
    super.onDestroy();
  }

  @Override public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflateAndBindViews(inflater, R.layout.fragment_branch_map, container);
    initializeMap(savedInstanceState);
    return view;
  }

  @Override protected void listenForEvents() {
    super.listenForEvents();
    getRxBus().event(ShowRouteOnMapEvent.class)
        .compose(this.bindToLifecycle())
        .subscribe(showRouteOnMapEvent -> {
          showRouteOnMap(showRouteOnMapEvent.getBranchModel());
        });

    getRxBus().event(ShowBranchMapInfoWindowEvent.class)
        .compose(this.bindToLifecycle())
        .subscribe(showBranchMapInfoWindowEvent -> {
          smoothShowInfoWindow(showBranchMapInfoWindowEvent.getBranchModel());
        });
  }

  private void initializeMap(Bundle savedInstanceState) {
    mapView.onCreate(savedInstanceState);
    enableMapGesturesInScrollView();
    mapView.getMapAsync(googleMap -> {
      map = googleMap;
      map.getUiSettings().setMyLocationButtonEnabled(true);
      map.getUiSettings().setZoomControlsEnabled(true);
      map.getUiSettings().setCompassEnabled(true);
      map.setMyLocationEnabled(true);
      MapsInitializer.initialize(getActivity());
      populateBranchesMap(branchModelList);
      weakHandler.postDelayed(() -> getRxBus().postIfHasObservers(new ScrollToMapEvent()), 500);
      map.setOnMarkerClickListener(marker -> {
        smoothShowInfoWindow(branchMap.get(marker));
        return true;
      });
    });
  }

  private void enableMapGesturesInScrollView() {
    imageOverMap.setOnTouchListener((v, event) -> {
      int action = event.getAction();
      switch (action) {
        case MotionEvent.ACTION_DOWN:
          rootLayout.requestDisallowInterceptTouchEvent(true);
          return false;
        case MotionEvent.ACTION_UP:
          rootLayout.requestDisallowInterceptTouchEvent(false);
          return true;
        case MotionEvent.ACTION_MOVE:
          rootLayout.requestDisallowInterceptTouchEvent(true);
          return false;
        default:
          return true;
      }
    });
  }

  private void pinHomeMarker(Location location) {
    if (location != null) {
      MapUtil.createMarker(map, location.getLatitude(), location.getLongitude(),
          R.drawable.home_pin_icon);
      MapUtil.goToPosition(map, location.getLatitude(), location.getLongitude(), false,
          Constant.CAMERA_ZOOM);
    }
  }

  private void populateBranchesMap(List<BranchModel> branchList) {
    map.clear();
    pinHomeMarker(myLastLocation);
    for (BranchModel branch : branchList) {
      Marker marker = MapUtil.createMarker(map, branch.getAddress().getLocation().getLat(),
          branch.getAddress().getLocation().getLng(), R.drawable.exchange_pin_icon);
      branchMap.put(marker, branch);
    }
  }

  private void smoothShowInfoWindow(BranchModel branch) {
    weakHandler.postDelayed(() -> DialogUtil.showBranchMapInfoWindow(getActivity(), branch), 100);
    weakHandler.postDelayed(
        () -> MapUtil.goToPosition(map, branch.getAddress().getLocation().getLat(),
            branch.getAddress().getLocation().getLng(), false), 200);
  }

  protected synchronized void buildGoogleApiClient() {
    googleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(
        new GoogleApiClient.ConnectionCallbacks() {
          @Override public void onConnected(@Nullable Bundle bundle) {
            myLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            pinHomeMarker(myLastLocation);
          }

          @Override public void onConnectionSuspended(int i) {
            googleApiClient.connect();
          }
        }).addOnConnectionFailedListener(connectionResult -> {

    }).addApi(LocationServices.API).build();
  }

  private void showRouteOnMap(BranchModel branch) {
    if (myLastLocation != null) {
      showLoadingDialog();
      Routing routing = new Routing.Builder().travelMode(AbstractRouting.TravelMode.DRIVING)
          .withListener(new RoutingListener() {
            @Override public void onRoutingFailure(RouteException e) {
              hideLoadingDialog();
              showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                  getString(R.string.error_something_gone_wrong));
            }

            @Override public void onRoutingStart() {

            }

            @Override public void onRoutingSuccess(ArrayList<Route> arrayList, int j) {
              hideLoadingDialog();
              MapUtil.goToPosition(map, branch.getAddress().getLocation().getLat(),
                  branch.getAddress().getLocation().getLng(), false);

              if (routePolylines.size() > 0) {
                for (Polyline poly : routePolylines) {
                  poly.remove();
                }
              }
              routePolylines = new ArrayList<>(arrayList.size());

              for (int i = 0; i < arrayList.size(); i++) {
                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(ContextCompat.getColor(getActivity(), R.color.mainColorBlue));
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(arrayList.get(i).getPoints());
                Polyline polyline = map.addPolyline(polyOptions);
                routePolylines.add(polyline);
              }
            }

            @Override public void onRoutingCancelled() {

            }
          })
          .alternativeRoutes(false)
          .waypoints(new LatLng(myLastLocation.getLatitude(), myLastLocation.getLongitude()),
              new LatLng(branch.getAddress().getLocation().getLat(),
                  branch.getAddress().getLocation().getLng()))
          .build();
      routing.execute();
    } else {
      showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
          getString(R.string.error_something_gone_wrong));
    }
  }
}
