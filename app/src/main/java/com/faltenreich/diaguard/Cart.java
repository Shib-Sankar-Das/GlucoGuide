package com.faltenreich.diaguard;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private List<Medicine> medicines;

    private Cart() {
        medicines = new ArrayList<>();
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }
}