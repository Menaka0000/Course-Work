package dto;

public class ItemDetails {
    private String orderId;
    private String itemCode;
    private double unitPrice;
    private int qtyForSell;
    private int updateStatus;

    public ItemDetails(String itemCode, double unitPrice, int qtyForSell, int updateStatus,String orderId) {
        this.itemCode = itemCode;
        this.unitPrice = unitPrice;
        this.qtyForSell = qtyForSell;
        this.updateStatus = updateStatus;
        this.orderId = orderId;
    }

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

    public void setUpdateStatus(int updateStatus) {
        this.updateStatus = updateStatus;
    }

    public int getUpdateStatus() {
        return updateStatus;
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
