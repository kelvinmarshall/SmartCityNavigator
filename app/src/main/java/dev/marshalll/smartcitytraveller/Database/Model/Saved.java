package dev.marshalll.smartcitytraveller.Database.Model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import io.reactivex.annotations.NonNull;

@Entity(tableName = "saved")
public class Saved {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name="id")
    public  int id;

    @ColumnInfo(name="name")
    public  String name;

    @ColumnInfo(name="image")
    public  String image;

    @ColumnInfo(name="location")
    public  String location;

    @ColumnInfo(name="category")
    public  String category;
}
