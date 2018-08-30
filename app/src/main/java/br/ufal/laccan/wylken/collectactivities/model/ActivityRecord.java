package br.ufal.laccan.wylken.collectactivities.model;

import java.util.Date;

public class ActivityRecord {

    private float x;
    private float y;
    private float z;
    private short activity_tag;
    private short person_tag;
    private short sensor_type;
    private Date time;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public short getActivity_tag() {
        return activity_tag;
    }

    public void setActivity_tag(short activity_tag) {
        this.activity_tag = activity_tag;
    }

    public short getPerson_tag() {
        return person_tag;
    }

    public void setPerson_tag(short person_tag) {
        this.person_tag = person_tag;
    }

    public short getSensor_type() {
        return sensor_type;
    }

    public void setSensor_type(short sensor_type) {
        this.sensor_type = sensor_type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
