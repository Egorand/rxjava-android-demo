package egorand.rxjavaandroiddemo.di;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import egorand.rxjavaandroiddemo.MainActivity;
import egorand.rxjavaandroiddemo.db.BeersDatabaseHelper;
import egorand.rxjavaandroiddemo.db.BeersLoader;
import egorand.rxjavaandroiddemo.model.Beer;
import egorand.rxjavaandroiddemo.rest.BeersRestClient;
import egorand.rxjavaandroiddemo.task.BeerLoadingTask;
import egorand.rxjavaandroiddemo.ui.BeersAdapter;
import egorand.rxjavaandroiddemo.ui.BeersFragment;
import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;

@Module(
        injects = {
                BeersFragment.class,
                BeersAdapter.class,
                BeerLoadingTask.class,
                BeersLoader.class
        }
)
public class MainModule {

    private final MainActivity activity;

    public MainModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    public Context provideContext() {
        return activity;
    }

    @Provides
    public LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(activity);
    }

    @Provides
    public Picasso providePicasso() {
        return Picasso.with(activity);
    }

    @Provides
    public BeersRestClient provideRestClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.brewerydb.com/v2")
                .setExecutors(AsyncTask.THREAD_POOL_EXECUTOR, new MainThreadExecutor())
                .build();
        return restAdapter.create(BeersRestClient.class);
    }

    @Provides @Singleton
    public BeersDatabaseHelper provideDatabaseHelper() {
        return OpenHelperManager.getHelper(activity, BeersDatabaseHelper.class);
    }

    @Provides @Singleton
    public RuntimeExceptionDao<Beer, String> provideBeersDao(BeersDatabaseHelper databaseHelper) {
        try {
            return RuntimeExceptionDao.createDao(databaseHelper.getConnectionSource(), Beer.class);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create DAO!");
        }
    }
}
