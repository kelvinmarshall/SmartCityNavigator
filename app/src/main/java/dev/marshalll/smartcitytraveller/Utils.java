package dev.marshalll.smartcitytraveller;

import dev.marshalll.smartcitytraveller.Database.DataSource.SavedRepository;
import dev.marshalll.smartcitytraveller.Database.Local.SavedRoomDatabase;
import dev.marshalll.smartcitytraveller.Remote.IGeoCoordinates;
import dev.marshalll.smartcitytraveller.Remote.RetrofitClient;

public class Utils {

    public static SavedRoomDatabase savedRoomDatabase;
    public static SavedRepository savedRepository;

    public static final String baseURl = "https://maps.googleapis.com";

    public static IGeoCoordinates getGeoCodeService(){
        return RetrofitClient.getClient(baseURl).create(IGeoCoordinates.class);
    }
}
