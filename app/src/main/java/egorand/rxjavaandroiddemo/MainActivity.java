package egorand.rxjavaandroiddemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import dagger.ObjectGraph;
import egorand.rxjavaandroiddemo.di.MainModule;

public class MainActivity extends Activity {

    private ObjectGraph objectGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        objectGraph = ObjectGraph.create(new MainModule(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }
}
