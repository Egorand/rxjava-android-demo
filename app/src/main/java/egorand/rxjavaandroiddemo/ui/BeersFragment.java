package egorand.rxjavaandroiddemo.ui;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import com.devspark.progressfragment.ProgressListFragment;

import java.util.List;

import de.greenrobot.event.EventBus;
import egorand.rxjavaandroiddemo.MainActivity;
import egorand.rxjavaandroiddemo.db.BeersLoader;
import egorand.rxjavaandroiddemo.model.Beer;
import egorand.rxjavaandroiddemo.task.BeerLoadingTask;

public class BeersFragment extends ProgressListFragment implements LoaderManager.LoaderCallbacks<List<Beer>> {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        setEmptyText("No beer today, sorry");
        loadData();
    }

    private void loadData() {
        BeerLoadingTask beerLoadingTask = ((MainActivity) getActivity()).getObjectGraph().get(BeerLoadingTask.class);
        beerLoadingTask.execute();
    }

    public void onEventMainThread(BeerLoadingTask.BeerLoadingResult result) {
        if (result.beers != null) {
            displayData(result.beers);
        } else {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    private void displayData(List<Beer> beers) {
        BeersAdapter adapter = ((MainActivity) getActivity()).getObjectGraph().get(BeersAdapter.class);
        adapter.setBeers(beers);
        setListAdapter(adapter);
    }

    @Override
    public Loader<List<Beer>> onCreateLoader(int id, Bundle args) {
        return ((MainActivity) getActivity()).getObjectGraph().get(BeersLoader.class);
    }

    @Override
    public void onLoadFinished(Loader<List<Beer>> loader, List<Beer> data) {
        if (isVisible()) {
            displayData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Beer>> loader) {
        // do nothing
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
