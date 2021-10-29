package controller;

import bo.BoFactory;
import bo.custom.*;
import dao.custom.impl.OrderDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import dto.ItemDTO;
import dto.ItemDetails;
import dto.OrderDTO;
import dto.tm.CartTm;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModifyOrderDetailController {
    public TextField txtOrderIdOrName;
    public ComboBox<String> cmbOrderIds;
    public TableView tblOderItem;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colUpdate;
    public TableColumn colUpdate1;
    public TableColumn colTotal;
    public TableColumn colClear;
    public ComboBox<String> cmbItemId;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtOrderQty;
    public Label lblTtl;
    public AnchorPane context;
    ObservableList<CartTm> obList1 = FXCollections.observableArrayList();
    /*
    * Property injection has been injected
    */
    private final PurchaseOrderBO purchaseOrderBO = (PurchaseOrderBO) BoFactory.getBoFactory().getBO(BoFactory.BoTypes.PURCHASE_ORDER);
    private final CustomerBO customerBO = (CustomerBO) BoFactory.getBoFactory().getBO(BoFactory.BoTypes.CUSTOMER);
    private final ItemBO itemBO = (ItemBO) BoFactory.getBoFactory().getBO(BoFactory.BoTypes.ITEM);
    private final OrderDetailBO orderDetailBO = (OrderDetailBO) BoFactory.getBoFactory().getBO(BoFactory.BoTypes.ORDER_DETAIL);
    private final UpdateOrderBO updateOrderBO = (UpdateOrderBO) BoFactory.getBoFactory().getBO(BoFactory.BoTypes.UPDATE_ORDER);

    public void initialize(){
        try {
            new NewOrderController().loadItemIds(cmbItemId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colClear.setCellValueFactory(new PropertyValueFactory<>("button"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("qtyUp"));
        colUpdate1.setCellValueFactory(new PropertyValueFactory<>("qtyDown"));

        cmbItemId.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    try {
                        setItemData(newValue);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
        cmbOrderIds.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    txtOrderIdOrName.setText(newValue);
                });
    }

    public void searchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        tblOderItem.getItems().clear();
        obList1.clear();
        if(containsDigit(txtOrderIdOrName.getText())){
            ArrayList<ItemDetails> all = orderDetailBO.getAllItemDetails();
            for(ItemDetails temp : all){
                if (temp.getOrderId().equals(txtOrderIdOrName.getText())){
                    ItemDTO itemDTO = itemBO.searchForItemId(temp.getItemCode());
                    Button btn=new Button("Clear");
                    Button qtyUp=new Button("+");
                    Button qtyDown=new Button("-");
                    CartTm tm=new CartTm(temp.getItemCode(), itemDTO.getDescription(),temp.getQtyForSell(),
                            itemDTO.getUnitPrice(),temp.getQtyForSell()* itemDTO.getUnitPrice(),btn,qtyUp,qtyDown,
                            itemDTO.getQtyOnHand(),
                            0);

                    btn.setOnAction((e)->{
                        obList1.remove(tm);
                        tm.setQtyOnHand(tm.getQtyOnHand()+tm.getQty());
                        setItemData(tm);
                    });

                    qtyUp.setOnAction((e)->{
                        if(tm.getQtyOnHand()-2<0){
                            new Alert(Alert.AlertType.WARNING,"Item is running out of stock").show();
                            return;
                        }
                        tm.setQty(tm.getQty()+2);
                        tm.setTotal(tm.getTotal()+ tm.getUnitPrice()*2);
                        calculateCost();
                        tblOderItem.refresh();
                        tm.setQtyOnHand(tm.getQtyOnHand()-2);
                        setItemData(tm);
                    });

                    qtyDown.setOnAction((e)->{
                        tm.setQty(tm.getQty()-2);
                        tm.setTotal(tm.getTotal()-tm.getUnitPrice()*2);
                        calculateCost();
                        tblOderItem.refresh();
                        tm.setQtyOnHand(tm.getQtyOnHand()+2);
                        setItemData(tm);
                    });
                    obList1.add(tm);
                    calculateCost();
                }
            }
            tblOderItem.setItems(obList1);
        }else {
            cmbOrderIds.getItems().clear();
            ArrayList<String> nameList=new ArrayList<>();
            List<String> ids=new ArrayList<>();
            ResultSet rst =customerBO.searchByName(txtOrderIdOrName.getText());
            while (rst.next()){nameList.add(rst.getString(1));}
            for (String x:nameList
                 ) {
                ResultSet rst1 =purchaseOrderBO.searchByCustomerId(x);
                while (rst1.next()){ids.add(rst1.getString(1));}
            }
            cmbOrderIds.getItems().addAll(ids);
        }
    }

    public void addOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String description = txtDescription.getText();
        int qtyForCustomer = Integer.parseInt(txtOrderQty.getText());
        double total = Integer.parseInt(txtOrderQty.getText())*Double.parseDouble(txtUnitPrice.getText());

        if (Integer.parseInt(txtQtyOnHand.getText())<Integer.parseInt(txtOrderQty.getText())){
            new Alert(Alert.AlertType.WARNING,"Stock Quantity exceeded").show();
            return;
        }
        Button btn=new Button("Clear");
        Button qtyUp=new Button("+");
        Button qtyDown=new Button("-");

        CartTm tm = new CartTm(
                cmbItemId.getValue(),
                description,
                Integer.parseInt(txtOrderQty.getText()),
                Double.parseDouble(txtUnitPrice.getText()),
                total,
                btn,
                qtyUp,
                qtyDown,
                Integer.parseInt(txtQtyOnHand.getText()),
                0
        );

        int rowNumber=isExists(tm);
        if (rowNumber==-1){
            obList1.add(tm);
            tm.setQtyOnHand(tm.getQtyOnHand()-tm.getQty());
            setItemData(tm);
            tm.setStatus(1);
            btn.setOnAction((e)->{
                obList1.remove(tm);
                tm.setQtyOnHand(tm.getQtyOnHand()+tm.getQty());
                setItemData(tm);
            });

            qtyUp.setOnAction((e)->{
                if(tm.getQtyOnHand()-2<0){
                    new Alert(Alert.AlertType.WARNING,"Item is running out of stock").show();
                    return;
                }
                tm.setQty(tm.getQty()+2);
                tm.setTotal(tm.getTotal()+ tm.getUnitPrice()*2);
                calculateCost();
                tblOderItem.refresh();
                tm.setQtyOnHand(tm.getQtyOnHand()-2);
                setItemData(tm);
            });

            qtyDown.setOnAction((e)->{
                tm.setQty(tm.getQty()-2);
                tm.setTotal(tm.getTotal()-tm.getUnitPrice()*2);
                calculateCost();
                tblOderItem.refresh();
                tm.setQtyOnHand(tm.getQtyOnHand()+2);
                setItemData(tm);
            });
        }else{
            CartTm temp = obList1.get(rowNumber);
            temp.setQty(temp.getQty()+qtyForCustomer);
            temp.setTotal(temp.getTotal()+total);
            calculateCost();
            temp.setQtyOnHand(temp.getQtyOnHand()-Integer.parseInt(txtOrderQty.getText()));
            tblOderItem.refresh();
            setItemData(temp);
        }
        tblOderItem.setItems(obList1);
        calculateCost();
    }

    private int isExists(CartTm tm){
        for (int i = 0; i < obList1.size(); i++) {
            if (tm.getCode().equals(obList1.get(i).getCode())){
                return i;
            }
        }
        return -1;
    }

    public void setItemData(String itemId) throws SQLException, ClassNotFoundException {
        ItemDTO itemDTO1 = itemBO.searchForItemId(itemId);
        txtDescription.setText(itemDTO1.getDescription());
        txtUnitPrice.setText(String.valueOf(itemDTO1.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(itemDTO1.getQtyOnHand()));
        cmbItemId.setValue(itemDTO1.getCode());
    }

    private void setItemData(CartTm cartTm){
        txtDescription.setText(cartTm.getDescription());
        txtUnitPrice.setText(String.valueOf(cartTm.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(cartTm.getQtyOnHand()));
        cmbItemId.setValue(cartTm.getCode());
    }

    void calculateCost(){
        double ttl=0;
        for (CartTm tm:obList1
        ) {
            ttl+=tm.getTotal();
        }
        lblTtl.setText(ttl+" /=");
    }

    public final boolean containsDigit(String s) {
        boolean containsDigit = false;
        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }
        return containsDigit;
    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(!containsDigit(txtOrderIdOrName.getText())){
            new Alert(Alert.AlertType.WARNING,"Select your order id to continue").show();
            return;
        }
        ArrayList<ItemDetails> items=new ArrayList<>();
        double total=0;
        for (CartTm temptm:obList1
        ) {
            total+=temptm.getTotal();
            items.add(new ItemDetails(temptm.getCode(),temptm.getUnitPrice(),temptm.getQty(),temptm.getStatus(),txtOrderIdOrName.getText()));
        }
        if (updateOrderBO.updateOrder(new OrderDTO(txtOrderIdOrName.getText(),total,items,cmbOrderIds))){
            for (CartTm cartTm : obList1) {     /*This for loop update each instance state to "previously added state,it's not new to the cart anymore"*/
                cartTm.setStatus(0);
            }
            /*searchOnAction(actionEvent);*/
            new Alert(Alert.AlertType.CONFIRMATION, "Order Modified successfully").show();
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again..").show();
        }
    }
    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       new OrderDAOImpl().deleteOrder(obList1,txtOrderIdOrName.getText());
       purchaseOrderBO.deleteOrder(obList1,txtOrderIdOrName.getText());
    }
}
