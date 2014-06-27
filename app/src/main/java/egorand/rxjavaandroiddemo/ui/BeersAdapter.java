package egorand.rxjavaandroiddemo.ui;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import egorand.rxjavaandroiddemo.R;
import egorand.rxjavaandroiddemo.model.Beer;

public class BeersAdapter extends RecyclerView.Adapter<BeersAdapter.BeerViewHolder> {

    private LayoutInflater layoutInflater;
    private Picasso picasso;

    private List<Beer> beers;

    @Inject
    public BeersAdapter(LayoutInflater layoutInflater, Picasso picasso) {
        this.layoutInflater = layoutInflater;
        this.picasso = picasso;
    }

    public void setBeers(List<Beer> beers) {
        this.beers = new ArrayList<Beer>(beers);
    }

    @Override
    public BeersAdapter.BeerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.row_beer, viewGroup, false);
        return new BeerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BeersAdapter.BeerViewHolder viewHolder, int i) {
        Beer beer = beers.get(i);
        if (!TextUtils.isEmpty(beer.getIcon())) {
            picasso.load(beer.getIcon()).into(viewHolder.icon);
        }
        viewHolder.title.setText(beer.getName());
        if (!TextUtils.isEmpty(beer.getAbv())) {
            viewHolder.abv.setText(beer.getAbv() + "%");
        }
        viewHolder.description.setText(beer.getDescription());
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    static class BeerViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.icon) ImageView icon;
        @InjectView(R.id.title) TextView title;
        @InjectView(R.id.description) TextView description;
        @InjectView(R.id.abv) TextView abv;

        BeerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
