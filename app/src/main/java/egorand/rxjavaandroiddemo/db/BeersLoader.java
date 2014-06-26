package egorand.rxjavaandroiddemo.db;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import egorand.rxjavaandroiddemo.model.Beer;

public class BeersLoader extends AbstractOrmLiteLoader<List<Beer>, String> {

    private final BeersDao beersDao;

    @Inject
    public BeersLoader(Context context, BeersDao beersDao) {
        super(context);
        this.beersDao = beersDao;
    }

    @Override
    public List<Beer> loadInBackground() {
        return beersDao.getCachedBeer();
    }
}
