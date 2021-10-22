package model;

public class ItemDetails {
    private String itemCode;
    private double unitPrice;
    private int qtyForSell;

    public ItemDetails() {
    }

    public ItemDetails(String itemCode, double unitPrice, int qtyForSell) {
        this.setItemCode(itemCode);
        this.setUnitPrice(unitPrice);
        this.setQtyForSell(qtyForSell);
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyForSell() {
        return qtyForSell;
    }

    public void setQtyForSell(int qtyForSell) {
        this.qtyForSell = qtyForSell;
    }

    @Override
    public String toString() {
        return "ItemDetails{" +
                "itemCode='" + itemCode + '\'' +
                ", unitPrice=" + unitPrice +
                ", qtyForSell=" + qtyForSell +
                '}';
    }
}
