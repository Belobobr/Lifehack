package md.fusionworks.lifehack.helper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ungvas on 9/29/15.
 */
public class MapHelper {

  public static void goToPosition(GoogleMap map, Double latitude, Double longitude,
      boolean animateCamera) {
    float zoom = map.getCameraPosition().zoom;
    goToPosition(map, latitude, longitude, animateCamera, zoom);
  }

  public static void goToPosition(GoogleMap map, Double latitude, Double longitude,
      boolean animateCamera, float zoom) {
    CameraPosition position =
        new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(zoom).build();
    if (animateCamera) {
      map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    } else {
      map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }
  }

  public static Marker createMarker(GoogleMap map, Double latitude, Double longitude,
      int iconResId) {
    LatLng latLng = new LatLng(latitude, longitude);
    return map.addMarker(
        new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(iconResId)));
  }
}