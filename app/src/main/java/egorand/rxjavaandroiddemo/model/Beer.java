package egorand.rxjavaandroiddemo.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Beers")
public class Beer {

    @DatabaseField(id = true) private String id;
    @DatabaseField private String name;
    @DatabaseField private String description;
    @DatabaseField private String abv;

    private Labels labels;
    @DatabaseField(useGetSet = true) private String icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getIcon() {
        if (labels == null) {
            return "";
        }
        return labels.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private class Labels {
        private String icon;
    }
}
