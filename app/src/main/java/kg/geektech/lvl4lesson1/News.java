package kg.geektech.lvl4lesson1;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity
public class News implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private long createdAt;

    public News() {
    }

    public News(String title, long createdAt) {
        this.title = title;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
