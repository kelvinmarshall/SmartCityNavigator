package dev.marshalll.smartcitytraveller.model;

public class Model_Nightlife {

    private String image_url;
    private String desc;
    private String details;
    private String name;
    private String opendays;
    private String openingtime;
    private String location;
    private String Lat;
    private String Long;

    public Model_Nightlife() {
    }

    public Model_Nightlife(String image_url, String desc, String details, String name, String opendays, String openingtime, String location, String lat, String aLong) {
        this.image_url = image_url;
        this.desc = desc;
        this.details = details;
        this.name = name;
        this.opendays = opendays;
        this.openingtime = openingtime;
        this.location = location;
        Lat = lat;
        Long = aLong;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpendays() {
        return opendays;
    }

    public void setOpendays(String opendays) {
        this.opendays = opendays;
    }

    public String getOpeningtime() {
        return openingtime;
    }

    public void setOpeningtime(String openingtime) {
        this.openingtime = openingtime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }
}
