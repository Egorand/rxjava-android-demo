package egorand.rxjavaandroiddemo;

import android.app.Application;
import android.os.StrictMode;

public class BeerApp extends Application {

    @Override public void onCreate() {
        super.onCreate();

        StrictMode.ThreadPolicy strictPolicy = new StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyDeath().build();
        StrictMode.setThreadPolicy(strictPolicy);
    }
}
