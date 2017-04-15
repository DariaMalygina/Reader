package com.example.asus.reader.db;


import android.os.Parcel;
import android.os.Parcelable;

public final class Feed implements Parcelable {
    private int idFeed = -1;
    private String nameFeed = "";
    private String urlFeed = "";

    private Feed(final Parcel in) {
        readFromParcel(in);
    }
    public Feed(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(idFeed);
        dest.writeString(nameFeed);
        dest.writeString(urlFeed);
    }


    private void readFromParcel(final Parcel in) {
        idFeed = in.readInt();
        nameFeed = in.readString();
        urlFeed = in.readString();
    }

    public static final Parcelable.Creator<Feed> CREATOR
            = new Parcelable.Creator<Feed>() {
        public Feed createFromParcel(final Parcel in) { return new Feed(in); }

        public Feed[] newArray(final int size) {
            return new Feed[size];
        }
    };





    void setIdFeed(final int idFeed) {
        this.idFeed = idFeed;
    }

    public String getNameFeed() {

        return nameFeed;
    }

    public void setNameFeed(final String nameFeed) {
        if(nameFeed != null)
            this.nameFeed = nameFeed;
    }

    public String getUrlFeed() {

        return urlFeed;
    }

    public void setUrlFeed(final String urlFeed) {
        if(urlFeed != null)
            this.urlFeed = urlFeed;
    }


}
