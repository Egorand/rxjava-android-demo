package egorand.rxjavaandroiddemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import egorand.rxjavaandroiddemo.model.Beer;

public class BeersDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "beers.db";
    private static final int DATABASE_VERSION = 1;

    private RuntimeExceptionDao<Beer, String> beersDao;

    public BeersDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Beer.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Beer.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RuntimeExceptionDao<Beer, String> getBeersDao() {
        if (beersDao == null) {
            try {
                beersDao = RuntimeExceptionDao.createDao(getConnectionSource(), Beer.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return beersDao;
    }
}
