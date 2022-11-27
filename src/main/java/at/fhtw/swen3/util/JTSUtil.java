package at.fhtw.swen3.util;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class JTSUtil {

    private JTSUtil() {
        /* prevent instantiation */
    }

    public static Geometry wktToGeometry(String wktString) {
        try {
            return new WKTReader().read(wktString);
        } catch (ParseException e) {
            return null;
        }
    }
}
