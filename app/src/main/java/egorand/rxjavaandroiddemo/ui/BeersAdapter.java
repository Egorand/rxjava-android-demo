package egorand.rxjavaandroiddemo.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class BeersAdapter extends BaseAdapter {

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
    public int getCount() {
        return beers.size();
    }

    @Override
    public Beer getItem(int position) {
        return beers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        BeerViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.row_beer, parent, false);
            viewHolder = new BeerViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (BeerViewHolder) view.getTag();
        }
        Beer beer = getItem(position);
        if (!TextUtils.isEmpty(beer.getIcon())) {
            picasso.load(beer.getIcon()).into(viewHolder.icon);
        }
        viewHolder.title.setText(beer.getName());
        if (!TextUtils.isEmpty(beer.getAbv())) {
            viewHolder.abv.setText(beer.getAbv() + "%");
        }
        viewHolder.description.setText(beer.getDescription());
        return view;
    }

    static class BeerViewHolder {
        @InjectView(R.id.icon) ImageView icon;
        @InjectView(R.id.title) TextView title;
        @InjectView(R.id.description) TextView description;
        @InjectView(R.id.abv) TextView abv;

        BeerViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
