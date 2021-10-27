package model.tm;

public class ItemTm {
    private String id;
    private String description;
    private int qtyOnHand;
    private double unitPrice;

    public ItemTm() {
    }

    public ItemTm(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public ItemTm(String id, String description, int qtyOnHand, double unitPrice) {
        this.setId(id);
        this.setDescription(description);
        this.setQtyOnHand(qtyOnHand);
        this.setUnitPrice(unitPrice);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
