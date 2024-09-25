package com.faltenreich.diaguard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class Medicine implements Serializable, Parcelable {
    private String name;
    private String description;
    private int availableUnits;
    private double price;
    private Timestamp manufacturingDate;
    private Timestamp expiryDate;
    private List<String> usedDrugs;
    private String imagePath;

    public Medicine() {}

    protected Medicine(Parcel in) {
        name = in.readString();
        description = in.readString();
        availableUnits = in.readInt();
        price = in.readDouble();
        manufacturingDate = in.readParcelable(Timestamp.class.getClassLoader());
        expiryDate = in.readParcelable(Timestamp.class.getClassLoader());
        usedDrugs = in.createStringArrayList();
        imagePath = in.readString();
    }

    public static final Parcelable.Creator<Medicine> CREATOR = new Parcelable.Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(availableUnits);
        dest.writeDouble(price);
        dest.writeParcelable(manufacturingDate, flags);
        dest.writeParcelable(expiryDate, flags);
        dest.writeStringList(usedDrugs);
        dest.writeString(imagePath);
    }

    // Getters and Setters
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

    public int getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(int availableUnits) {
        this.availableUnits = availableUnits;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(Timestamp manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<String> getUsedDrugs() {
        return usedDrugs;
    }

    public void setUsedDrugs(List<String> usedDrugs) {
        this.usedDrugs = usedDrugs;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}