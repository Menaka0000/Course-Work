package controller;

import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dao.custom.impl.CustomerDAOImpl;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OrderDAOImpl;
import dao.custom.impl.OrderDetailDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Item;
import model.ItemDetails;
import model.Order;
import model.tm.CartTm;
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
    ObservableList<CartTm> obList1= FXCollections.observableArrayList();
    /*
    * Property injection has been injected
    */
    private final ItemDAO itemDAO = new ItemDAOImpl();
    private final CustomerDAO customerDAO=new CustomerDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final OrderDetailDAO orderDetailDAO= new OrderDetailDAOImpl();

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
        if(containsDigit(txtOrderIdOrName.getText())){
            ArrayList<ItemDetails> all = orderDetailDAO.getAll();
            for(ItemDetails temp : all){
                if (temp.getOrderId().equals(txtOrderIdOrName.getText())){
                    Item item = itemDAO.search(temp.getItemCode());
                    Button btn=new Button("Clear");
                    Button qtyUp=new Button("+");
                    Button qtyDown=new Button("-");
                    CartTm tm=new CartTm(temp.getItemCode(),item.getDescription(),temp.getQtyForSell(),
                            item.getUnitPrice(),temp.getQtyForSell()* item.getUnitPrice(),btn,qtyUp,qtyDown);
                    new NewOrderController().setButtons(tm,tblOderItem,btn,qtyUp,qtyDown);
                    obList1.add(tm);
                    calculateCost();
                }
            }
            tblOderItem.setItems(obList1);
        }else {
            cmbOrderIds.getItems().clear();
            ArrayList<String> nameList=new ArrayList<>();
            List<String> ids=new ArrayList<>();
            ResultSet rst = customerDAO.searchByName(txtOrderIdOrName.getText());
            while (rst.next()){nameList.add(rst.getString(1));}
            for (String x:nameList
                 ) {
                ResultSet rst1 =orderDAO.searchByCustomerId(x);
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
                Integer.parseInt(txtQtyOnHand.getText())
        );

        int rowNumber=isExists(tm);
        if (rowNumber==-1){
            obList1.add(tm);
            tm.setQtyOnHand(tm.getQtyOnHand()-tm.getQty());
            setItemData(tm);

            new NewOrderController().setButtons(tm,tblOderItem,btn,qtyUp,qtyDown);


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
        Item item1 = itemDAO.search(itemId);
        txtDescription.setText(item1.getDescription());
        txtUnitPrice.setText(String.valueOf(item1.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(item1.getQtyOnHand()));
        cmbItemId.setValue(item1.getCode());
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
            items.add(new ItemDetails(temptm.getCode(),temptm.getUnitPrice(),temptm.getQty()));
        }
        new OrderDAOImpl().update(new Order(txtOrderIdOrName.getText(),total,items,cmbOrderIds));
    }

    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       new OrderDAOImpl().deleteOrder(obList1,txtOrderIdOrName.getText());
    }
}
