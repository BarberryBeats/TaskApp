package kg.geektech.lvl4lesson1.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import kg.geektech.lvl4lesson1.News;

@Database(entities = {News.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
}