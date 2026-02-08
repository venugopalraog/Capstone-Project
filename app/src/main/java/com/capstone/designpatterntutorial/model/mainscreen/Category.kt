package com.capstone.designpatterntutorial.model.mainscreen;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

/**
 * Created by venugopalraog on 4/24/17.
 */

public class Category implements Parcelable {

    public static final int INITIAL_ODD_NUMBER = 17;
    public static final int MULTIPLIER_ODD_NUMBER = 37;
    private int id;
    private String name;
    private String description;
    private ArrayList<Pattern> patternList;


    public Category() {
    }

    protected Category(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        patternList = in.createTypedArrayList(Pattern.CREATOR);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeTypedList(patternList);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Pattern> getPatternList() {
        return patternList;
    }

    public void setPatternList(ArrayList<Pattern> patternList) {
        this.patternList = patternList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return new EqualsBuilder()
                .append(id, category.id)
                .append(name, category.name)
                .append(description, category.description)
                .append(patternList, category.patternList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INITIAL_ODD_NUMBER, MULTIPLIER_ODD_NUMBER)
                .append(id)
                .append(name)
                .append(description)
                .append(patternList)
                .toHashCode();
    }
}
