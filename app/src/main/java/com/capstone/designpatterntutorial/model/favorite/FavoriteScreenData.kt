package com.capstone.designpatterntutorial.model.favorite;

import android.os.Parcel;
import android.os.Parcelable;

import com.capstone.designpatterntutorial.model.mainscreen.Pattern;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created by gubbave on 7/12/2017.
 */

public class FavoriteScreenData implements Parcelable {

    public static final int INITIAL_ODD_NUMBER = 17;
    public static final int MULTIPLIER_ODD_NUMBER = 37;
    private ArrayList<Pattern> patternList;

    public FavoriteScreenData() {
    }

    protected FavoriteScreenData(Parcel in) {
        patternList = in.createTypedArrayList(Pattern.CREATOR);
    }

    public static final Creator<FavoriteScreenData> CREATOR = new Creator<FavoriteScreenData>() {
        @Override
        public FavoriteScreenData createFromParcel(Parcel in) {
            return new FavoriteScreenData(in);
        }

        @Override
        public FavoriteScreenData[] newArray(int size) {
            return new FavoriteScreenData[size];
        }
    };

    public ArrayList<Pattern> getPatternList() {
        return patternList;
    }

    public void setPatternList(ArrayList<Pattern> patternList) {
        this.patternList = patternList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(patternList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        FavoriteScreenData that = (FavoriteScreenData) obj;

        return new EqualsBuilder()
                .append(patternList, that.patternList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INITIAL_ODD_NUMBER, MULTIPLIER_ODD_NUMBER)
                .append(patternList)
                .toHashCode();
    }
}
