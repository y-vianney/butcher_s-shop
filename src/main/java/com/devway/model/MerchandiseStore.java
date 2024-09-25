package com.devway.model;

import java.util.HashMap;
import java.util.Map;

public class MerchandiseStore {
    private Map<String, Merchandise> merchandiseMap;

    public MerchandiseStore() {
        this.merchandiseMap = new HashMap<>();
    }

    // Method to add merchandise
    public void addMerchandise(String key, Merchandise merchandise) {
        this.merchandiseMap.put(key, merchandise);
        this.merchandiseMap.put(key, merchandise);
    }

    // Method to get merchandise by key
    public Merchandise getMerchandise(String key) {
        return this.merchandiseMap.get(key);
    }

    public Map<String, Merchandise> getStore() {
        return this.merchandiseMap;
    }

    // Method to display all merchandise
    public void displayMerchandise(Double total, Double leftOver) {
        System.out.println(
            "------------------------------------------\n\n" +
            "\t\t Boucherie \n" +
            "\t No 00000 Abidjan Plateau \n" +
            "\t       +225 0102000304 \n" +
            "\n------------------------------------------\n" +
            " Produits \t\t\t Montant \n"
        );

        for (Map.Entry<String, Merchandise> entry : merchandiseMap.entrySet()) {
            System.out.println(
                entry.getValue().getName() +
                "\t " + entry.getValue().getQuantity() + " * " +
                entry.getValue().getPrice() + "\t\t" +
                (entry.getValue().getPrice() * entry.getValue().getQuantity())
            );
        }

        System.out.println(
            "------------------------------------------\n\n" +
            " Montant total : \t\t" + total + "\n" +
            " Reste à payer : \t\t" + leftOver + "\n" +
            "\n------------------------------------------\n\n" +
            "\t       A la prochaine."
        );
    }
}
