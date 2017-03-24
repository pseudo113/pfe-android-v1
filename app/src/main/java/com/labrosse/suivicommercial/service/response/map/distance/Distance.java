package com.labrosse.suivicommercial.service.response.map.distance;

/**
 * Created by ahmedhammami on 28/05/16.
 */
public class Distance implements Comparable<Distance> {
    int index;
    int distance;

    public Distance(int index, int distance) {
        this.index = index;
        this.distance = distance;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Distance distance1) {
        return getDistance() - distance1.getDistance();
    }

}
