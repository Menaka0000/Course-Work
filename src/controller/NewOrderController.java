package controller;

import bo.custom.PurchaseOrderBO;
import bo.custom.impl.PurchaseOrderBOImpl;
import dao.custom.CustomerDAO;
import dao.custom.OrderDAO;
import dao.custom.impl.CustomerDAOImpl;
import dao.custom.ItemDAO;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OrderDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.Item;
import model.ItemDetails;
import model.Order;
import model.tm.CartTm;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewOrderController {
    public ComboBox<String> cmbCustId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtPostalCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtOrderQty;
    public ComboBox<String> cmbItemId;
    public TableView tblCart;
    public Label lblTtl;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colUpdate;
    public TableColumn colTotal;
    public TableColumn colClear;
    public TableColumn colUpdate1;
    public Label lblOrderId;
    ObservableList<CartTm> obList= FXCollections.observableArrayList();

    /* Property injection has been injected */
   /* private final CustomerDAO customerDAO = new CustomerDAOImpl();
    private final ItemDAO itemDAO = new ItemDAOImpl();*/
    private final PurchaseOrderBO purchaseOrderBO = new PurchaseOrderBOImpl();

    public void initialize()  {

        txtDescription.setEditable(false);
        txtUnitPrice.setEditable(false);
        txtQtyOnHand.setEditable(false);
        colClear.setStyle("-fx-alignment: CENTER;");
        colTotal.setStyle("-fx-alignment: CENTER;");
        colUpdate1.setStyle("-fx-alignment: CENTER;");
        colUpdate.setStyle("-fx-alignment: CENTER;");
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colClear.setCellValueFactory(new PropertyValueFactory<>("button"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("qtyUp"));
        colUpdate1.setCellValueFactory(new PropertyValueFactory<>("qtyDown"));

        try {
            setOrderId();
            loadCustomerIds();
            loadItemIds(cmbItemId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbCustId.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    try {
                        setCustomerData(newValue);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });

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
    }

    private void setOrderId() throws SQLException, ClassNotFoundException {
        lblOrderId.setText(new OrderDAOImpl().getOrderId());
    }

    public void logOutOnAction(ActionEvent actionEvent) {
    }

    public void addToCartOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
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

            int rowNumber=isExists(tm.getCode());
            if (rowNumber==-1){
                obList.add(tm);
                tm.setQtyOnHand(tm.getQtyOnHand()-tm.getQty());
                setItemData(tm);
                setButtons(tm,tblCart,btn,qtyUp,qtyDown);
            }else{
                CartTm temp = obList.get(rowNumber);
                temp.setQty(temp.getQty()+qtyForCustomer);
                temp.setTotal(temp.getTotal()+total);
                calculateCost();
                temp.setQtyOnHand(temp.getQtyOnHand()-Integer.parseInt(txtOrderQty.getText()));
                tblCart.refresh();
                setItemData(temp);
            }
            tblCart.setItems(obList);
            calculateCost();
    }

    public void setButtons(CartTm tm,TableView tableView,Button btn,Button qtyUp,Button qtyDown){
        btn.setOnAction((e)->{
            obList.remove(tm);
            tm.setQtyOnHand(tm.getQtyOnHand()+tm.getQty());
            try {
                setItemData(tm);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        qtyUp.setOnAction((e)->{
            if(tm.getQtyOnHand()-2<0){
                new Alert(Alert.AlertType.WARNING,"Item is running out of stock").show();
                return;
            }
            tm.setQty(tm.getQty()+2);
            tm.setTotal(tm.getTotal()+ tm.getUnitPrice()*2);
            calculateCost();
            tblCart.refresh();
            tm.setQtyOnHand(tm.getQtyOnHand()-2);
            try {
                setItemData(tm);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });

        qtyDown.setOnAction((e)->{
            tm.setQty(tm.getQty()-2);
            tm.setTotal(tm.getTotal()-tm.getUnitPrice()*2);
            calculateCost();
            tblCart.refresh();
            tm.setQtyOnHand(tm.getQtyOnHand()+2);
            try {
                setItemData(tm);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    private int isExists(String id){
        for (int i = 0; i < obList.size(); i++) {
            if (id.equals(obList.get(i).getCode())){
                return i;
            }
        }
        return -1;
    }

    void calculateCost(){
        double ttl=0;
        for (CartTm tm:obList
        ) {
            ttl+=tm.getTotal();
        }
        lblTtl.setText(ttl+"");
    }

    private void loadCustomerIds() throws SQLException, ClassNotFoundException {
        List<String> customerIds = customerDAO.getCustomerIds();
        cmbCustId.getItems().addAll(customerIds);
    }

    public void loadItemIds(ComboBox<String> cmbItemId) throws SQLException, ClassNotFoundException {
        List<String> ItemIds = itemDAO.getItemIds();
        cmbItemId.getItems().addAll(ItemIds);
    }

    public void setCustomerData(String customerId) throws SQLException, ClassNotFoundException {
        Customer c1 = customerDAO.search(customerId);
        if (c1 == null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set");
        } else {
            txtName.setText(c1.getName());
            txtPostalCode.setText(c1.getPostalCode());
            txtAddress.setText(c1.getAddress());
        }
    }

    public void setItemData(String id) throws SQLException, ClassNotFoundException {
        Item item1 = itemDAO.search(id);
        txtDescription.setText(item1.getDescription());
        txtUnitPrice.setText(String.valueOf(item1.getUnitPrice()));
        cmbItemId.setValue(item1.getCode());
        if (isExists(id)==-1){
            txtQtyOnHand.setText(String.valueOf(item1.getQtyOnHand()));
        }else {
            for (CartTm temp:obList){
                if (temp.getCode().equals(id)){
                    txtQtyOnHand.setText(String.valueOf(temp.getQtyOnHand()));
                    break;
                }
            }
        }
      }

    private void setItemData(CartTm cartTm) throws SQLException, ClassNotFoundException {
            txtDescription.setText(cartTm.getDescription());
            txtUnitPrice.setText(String.valueOf(cartTm.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(cartTm.getQtyOnHand()));
            cmbItemId.setValue(cartTm.getCode());
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss a");

        ArrayList<ItemDetails> items = new ArrayList<>();
        double total=0;
        for (CartTm temptm:obList
             ) {
            total+=temptm.getTotal();
            items.add(new ItemDetails(lblOrderId.getText(),temptm.getCode(),temptm.getUnitPrice(),temptm.getQty()));
        }
        Order order=new Order(
                lblOrderId.getText(),
                cmbCustId.getValue(),
                f.format(date),
                sdf.format(new Date()),
                total,
                items
        );
        if (purchaseOrderBO.purchaseOrder(order)){
            tblCart.getItems().clear();
            lblOrderId.setText(new OrderDAOImpl().getOrderId());                                      //you have to fix this...
            new Alert(Alert.AlertType.CONFIRMATION,"Order Placed..").show();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again..").show();
        }
    }

    public void cancelOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        tblCart.getItems().clear();
        setItemData(cmbItemId.getValue());
    }
}
