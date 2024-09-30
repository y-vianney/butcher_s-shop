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
    public String displayMerchandise(String date, Double total, Double leftOver, Supplier supplier) {
        StringBuilder output = new StringBuilder();
    
        output.append("-------------------------------------------------------------\n")
              .append("                               Boucherie\n")
              .append("                     No 00000 Abidjan Plateau\n")
              .append("                          +225 0102000304\n")
              .append("                             ").append(date).append("\n")
              .append("                 Fournisseur: ").append(supplier.getName())
              .append(" - ").append(supplier.getContact()).append("\n")
              .append("-------------------------------------------------------------\n")
              .append("   Produits                                              Montant\n")
              .append("-------------------------------------------------------------\n  ");
    
        for (Map.Entry<String, Merchandise> entry : merchandiseMap.entrySet()) {
            output.append(entry.getValue().getName())
                  .append("     ")
                  .append(entry.getValue().getQuantity())
                  .append(" X ")
                  .append(entry.getValue().getPrice())
                  .append("                         ")
                  .append(entry.getValue().getPrice() * entry.getValue().getQuantity())
                  .append("\n  ");
        }
    
        output.append("\n-------------------------------------------------------------\n")
              .append("   Montant total :                               ").append(total).append("\n")
              .append("   Reste à payer :                               ").append(leftOver).append("\n")
              .append("-------------------------------------------------------------\n");
    
        return output.toString();
    }
    
}
