package egorand.rxjavaandroiddemo.model;

import java.util.ArrayList;
import java.util.List;

public class BeerContainer {
    private List<Beer> data;

    public BeerContainer() {
    }

    public BeerContainer(List<Beer> beers) {
        this.data = new ArrayList<Beer>(beers);
    }

    public List<Beer> getBeer() {
        return data;
    }
}
