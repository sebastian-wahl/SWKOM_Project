package at.fhtw.swen3.util;

import at.fhtw.swen3.gps.service.models.GeoEncodingCoordinate;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.Locale;

@Slf4j
public class JTSUtil {

    private static WKTReader wktReader;

    private JTSUtil() {
        /* prevent instantiation */
    }

    public static Geometry geoEncodingCoordinateToGeometry(GeoEncodingCoordinate geoEncodingCoordinate) {
        String wktPoint = String.format(Locale.US, "POINT(%s %s)", geoEncodingCoordinate.getLat(), geoEncodingCoordinate.getLon());
        return wktToGeometry(wktPoint);
    }

    public static Geometry wktToGeometry(String lat, String lon) {
        return wktToGeometry(String.format(Locale.US, "POINT(%s %s)", lat, lon));
    }

    public static Geometry wktToGeometry(double lat, double lon) {
        return wktToGeometry(String.format(Locale.US, "POINT(%f %f)", lat, lon));
    }

    private static Geometry wktToGeometry(String wktString) {
        if (wktReader == null) {
            wktReader = new WKTReader();
        }

        try {
            return wktReader.read(wktString);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
