package egorand.rxjavaandroiddemo.db;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import egorand.rxjavaandroiddemo.model.Beer;

@Singleton
public class BeersDao {

    private final Lazy<RuntimeExceptionDao<Beer, String>> beerDao;

    @Inject
    public BeersDao(Lazy<RuntimeExceptionDao<Beer, String>> beerDao) {
        this.beerDao = beerDao;
    }

    public List<Beer> getCachedBeer() {
        return beerDao.get().queryForAll();
    }

    public void cacheBeer(final List<Beer> beers) {
        beerDao.get().callBatchTasks(new Callable<Void>() {
            @Override public Void call() throws Exception {
                for (Beer beer : beers) {
                    beerDao.get().createOrUpdate(beer);
                }
                return null;
            }
        });
    }
}
