package model;

public class ItemDetails {
    private String orderId;
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

    public ItemDetails(String orderId, String itemCode, double unitPrice, int qtyForSell) {
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.unitPrice = unitPrice;
        this.qtyForSell = qtyForSell;
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

    public String getOrderId() {return orderId; }


    @Override
    public String toString() {
        return "ItemDetails{" +
                "itemCode='" + itemCode + '\'' +
                ", unitPrice=" + unitPrice +
                ", qtyForSell=" + qtyForSell +
                '}';
    }
}
