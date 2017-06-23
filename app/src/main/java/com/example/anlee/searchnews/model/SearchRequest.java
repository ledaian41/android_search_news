package com.example.anlee.searchnews.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

import static com.example.anlee.searchnews.model.Constant.BEGIN_DATE;
import static com.example.anlee.searchnews.model.Constant.QUERY;
import static com.example.anlee.searchnews.model.Constant.SORT;

/**
 * Created by An Lee on 6/21/2017.
 */

public class SearchRequest implements Parcelable {
    private int page = 0;
    private String query;
    private String beginDate;
    private String order = "newest";
    private boolean hasArts;
    private boolean hasFashionAndStyle;
    private boolean hasSport;

    public SearchRequest() {
    }

    protected SearchRequest(Parcel in) {
        page = in.readInt();
        query = in.readString();
        beginDate = in.readString();
        order = in.readString();
        hasArts = in.readByte() != 0;
        hasFashionAndStyle = in.readByte() != 0;
        hasSport = in.readByte() != 0;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getQuery() {
        return query;
    }

    public String getBeginDate() {
        if (beginDate != null) {
            return beginDate.substring(6, 8)
                    + "/" + beginDate.substring(4, 6)
                    + "/" + beginDate.substring(0, 4);
        }
        return null;
    }

    public void setBeginDate(String beginDate) {
        String[] date = beginDate.split("/");
        this.beginDate = date[2] + date[1] + date[0];
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public boolean isHasArts() {
        return hasArts;
    }

    public void setHasArts(boolean hasArts) {
        this.hasArts = hasArts;
    }

    public boolean isHasFashionAndStyle() {
        return hasFashionAndStyle;
    }

    public void setHasFashionAndStyle(boolean hasFashionAndStyle) {
        this.hasFashionAndStyle = hasFashionAndStyle;
    }

    public boolean isHasSport() {
        return hasSport;
    }

    public void setHasSport(boolean hasSport) {
        this.hasSport = hasSport;
    }

    public static final Creator<SearchRequest> CREATOR = new Creator<SearchRequest>() {
        @Override
        public SearchRequest createFromParcel(Parcel in) {
            return new SearchRequest(in);
        }

        @Override
        public SearchRequest[] newArray(int size) {
            return new SearchRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeString(query);
        dest.writeString(beginDate);
        dest.writeString(order);
        dest.writeByte((byte) (hasArts ? 1 : 0));
        dest.writeByte((byte) (hasFashionAndStyle ? 1 : 0));
        dest.writeByte((byte) (hasSport ? 1 : 0));
    }

    public Map<String, String> toQueryMap() {
        Map<String, String> options = new HashMap<>();
        if (query != null && query.length() > 0) options.put(QUERY, query);
        if (beginDate != null && beginDate.length() > 0) options.put(BEGIN_DATE, beginDate);
        if (order != null) options.put(SORT, order.toLowerCase());
        if (getNewDesk() != null && getNewDesk().length() > 0) {
            options.put("fq", "news_desk:(" + getNewDesk() + ")");
        }
        options.put("page", String.valueOf(page));
        return options;
    }

    private String getNewDesk() {
        if (!hasArts && !hasFashionAndStyle && !hasSport) {
            return null;
        }
        String value = "";
        if (hasArts) value += "/arts/";
        if (hasFashionAndStyle) value += "/fashion/style";
        if (hasSport) value += "/sports/";
        return value;
    }

    public void resetPage() {
        page = 0;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void nextPage() {
        page += 1;

    }
}
