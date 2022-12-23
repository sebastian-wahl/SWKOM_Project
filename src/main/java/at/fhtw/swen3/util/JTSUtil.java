package at.fhtw.swen3.util;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class JTSUtil {

    private static WKTReader wktReader;

    private JTSUtil() {
        /* prevent instantiation */
    }

    public static Geometry wktToGeometry(String wktString) {
        if (wktReader == null) {
            wktReader = new WKTReader();
        }

        try {
            return wktReader.read(wktString);
        } catch (ParseException e) {
            return null;
        }
    }
}
