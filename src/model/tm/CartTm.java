package model.tm;

import javafx.scene.control.Button;

public class CartTm {
    private String code;
    private String description;
    private int qty;
    private double unitPrice;
    private double total;
    private Button button ;
    private Button qtyUp ;
    private Button qtyDown ;

    public CartTm() {
    }

    public CartTm(String code, String description, int qty, double unitPrice, double total, Button button, Button qtyUp, Button qtyDown) {
        this.setCode(code);
        this.setDescription(description);
        this.setQty(qty);
        this.setUnitPrice(unitPrice);
        this.setTotal(total);
        this.setButton(button);
        this.setQtyUp(qtyUp);
        this.setQtyDown(qtyDown);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getQtyUp() {
        return qtyUp;
    }

    public void setQtyUp(Button qtyUp) {
        this.qtyUp = qtyUp;
    }

    public Button getQtyDown() {
        return qtyDown;
    }

    public void setQtyDown(Button qtyDown) {
        this.qtyDown = qtyDown;
    }

    @Override
    public String toString() {
        return "CartTm{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", total=" + total +
                ", button=" + button +
                ", qtyUp=" + qtyUp +
                ", qtyDown=" + qtyDown +
                '}';
    }
}
