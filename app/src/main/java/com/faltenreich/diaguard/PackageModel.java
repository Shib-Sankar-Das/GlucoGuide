// PackageModel.java
package com.faltenreich.diaguard;

import java.io.Serializable;

public class PackageModel implements Serializable {
    private String packageName;
    private int price;
    private String description;
    private String prerequisites;
    private String image;

    public PackageModel() {
        // Default constructor required for calls to DataSnapshot.getValue(PackageModel.class)
    }

    public PackageModel(String packageName, int price, String description, String prerequisites, String image) {
        this.packageName = packageName;
        this.price = price;
        this.description = description;
        this.prerequisites = prerequisites;
        this.image = image;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}