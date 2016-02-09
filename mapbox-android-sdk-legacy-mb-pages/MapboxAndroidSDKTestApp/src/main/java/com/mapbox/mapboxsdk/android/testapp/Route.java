package com.mapbox.mapboxsdk.android.testapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Route implements Parcelable {
    private int routeNumber;
    private String distance;

    public Route(int routeNumber, String distance) {
        this.routeNumber = routeNumber;
        this.distance = distance;
    }

    public int getRouteNumber() {
        return routeNumber;
    }


    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeInt(routeNumber);
        destination.writeString(distance);
    }

    public String getDistance() {
        return distance;
    }

    protected Route(Parcel in) {
        routeNumber = in.readInt();
        distance = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {

        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };
}
