package com.example.asus.reader.db;


import android.os.Parcel;
import android.os.Parcelable;

public final class Item implements Parcelable{

    private int idItem = -1;
    private String urlFeed = "";
    private String titleItem = "";
    private String textItem = "";
    private String dateItem = "";
    private String urlItem = "";

    private Item(final Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(idItem);
        dest.writeString(urlFeed);
        dest.writeString(titleItem);
        dest.writeString(textItem);
        dest.writeString(dateItem);
        dest.writeString(urlItem);
    }

    private void readFromParcel(final Parcel in) {
        idItem = in.readInt();
        urlFeed = in.readString();
        titleItem = in.readString();
        textItem = in.readString();
        dateItem = in.readString();
        urlItem = in.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR
            = new Parcelable.Creator<Item>() {
        public Item createFromParcel(final Parcel in) { return new Item(in); }

        public Item[] newArray(final int size) {
            return new Item[size];
        }
    };

    public Item(){}


    void setIdItem(final int idItem) {
        this.idItem = idItem;
    }

    public String getUrlFeed() {
        return urlFeed;
    }

    public void setUrlFeed(final String idFeed) {
        this.urlFeed = idFeed;
    }

    public String getTitleItem() {
        return titleItem;
    }

    public void setTitleItem(final String titleItem) {
        if(titleItem != null)
            this.titleItem = titleItem;

    }

    public String getTextItem() {
        return textItem;
    }

    public void setTextItem(final String textItem) {
        if(textItem != null)
            this.textItem = textItem;
    }

    public String getDateItem() {
        return dateItem;
    }

    public void setDateItem(final String dateItem) {
        if(dateItem != null)
            this.dateItem = dateItem;
    }

    public String getUrlItem() {
        return urlItem;
    }

    public void setUrlItem(final String urlItem) {
        if(urlItem != null)
            this.urlItem = urlItem;
    }
}
