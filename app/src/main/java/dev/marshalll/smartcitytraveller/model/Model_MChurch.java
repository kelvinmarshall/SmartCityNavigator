package dev.marshalll.smartcitytraveller.model;

public class Model_MChurch {

    private String image_url;
    private String desc;
    private String details;
    private String name;
    private String service_start;
    private String service_end;
    private String opendays;
    private String openingtime;
    private String location;
    private String Lat;
    private String Long;

    public Model_MChurch() {
    }

    public Model_MChurch(String image_url, String desc, String details, String name, String service_start, String service_end, String opendays, String openingtime, String location, String lat, String aLong) {
        this.image_url = image_url;
        this.desc = desc;
        this.details = details;
        this.name = name;
        this.service_start = service_start;
        this.service_end = service_end;
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

    public String getService_start() {
        return service_start;
    }

    public void setService_start(String service_start) {
        this.service_start = service_start;
    }

    public String getService_end() {
        return service_end;
    }

    public void setService_end(String service_end) {
        this.service_end = service_end;
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
