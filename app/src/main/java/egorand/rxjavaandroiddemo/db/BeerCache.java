package egorand.rxjavaandroiddemo.db;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import dagger.Lazy;
import egorand.rxjavaandroiddemo.model.Beer;

public class BeerCache {

    private final Lazy<RuntimeExceptionDao<Beer, String>> beersDao;

    @Inject
    public BeerCache(@BeersDao Lazy<RuntimeExceptionDao<Beer, String>> beersDao) {
        this.beersDao = beersDao;
    }

    public void cacheBeer(final List<Beer> beers) {
        beersDao.get().callBatchTasks(new Callable<Void>() {
            @Override public Void call() throws Exception {
                for (Beer beer : beers) {
                    beersDao.get().createOrUpdate(beer);
                }
                return null;
            }
        });
    }
}
