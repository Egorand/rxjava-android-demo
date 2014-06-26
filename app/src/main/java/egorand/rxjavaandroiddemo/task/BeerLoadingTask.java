package egorand.rxjavaandroiddemo.task;

import android.os.AsyncTask;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import egorand.rxjavaandroiddemo.db.BeersDao;
import egorand.rxjavaandroiddemo.model.Beer;
import egorand.rxjavaandroiddemo.rest.BeersRestClient;

public class BeerLoadingTask extends AsyncTask<Void, Void, Void> {

    private final BeersRestClient beersRestClient;
    private final BeersDao beersDao;

    @Inject
    public BeerLoadingTask(BeersRestClient beersRestClient, BeersDao beersDao) {
        this.beersRestClient = beersRestClient;
        this.beersDao = beersDao;
    }

    @Override protected Void doInBackground(Void... params) {
        try {
            List<Beer> beers = beersRestClient.getBeers("5").getBeer();
            beersDao.cacheBeer(beers);
            EventBus.getDefault().post(new BeerLoadingResult(beers, null));
        } catch (Exception e) {
            EventBus.getDefault().post(new BeerLoadingResult(null, e));
        }
        return null;
    }

    public static class BeerLoadingResult {
        public final List<Beer> beers;
        public final Exception exception;

        BeerLoadingResult(List<Beer> beers, Exception exception) {
            this.beers = beers;
            this.exception = exception;
        }
    }
}
