package controller;

import dao.custom.CustomerDAO;
import dao.custom.impl.CustomerDAOImpl;
import dao.custom.ItemDAO;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OrderDAOImpl;
import db.DbConnection;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    static int variable=0;

    /* Property injection has been injected */
    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    private final ItemDAO itemDAO = new ItemDAOImpl();

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
                        setItemData(newValue, txtDescription, txtUnitPrice, txtQtyOnHand, cmbItemId);
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

    public void resetItems() throws SQLException, ClassNotFoundException {
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `tempItem`");
        ResultSet rst=pst.executeQuery();

        while(rst.next()){
            if(rst.getString(1).equals(cmbItemId.getValue())){
                System.out.println("test1");
                PreparedStatement pst1 = DbConnection.getInstance().getConnection().prepareStatement("UPDATE `tempItem` set qtyOnHand=? WHERE ItemCode=?");
                pst1.setObject(1,txtQtyOnHand.getText());
                pst1.setObject(2,rst.getString(1));
                pst1.executeUpdate();
                return;
            }
        }
        System.out.println("test2");
        PreparedStatement pst2 = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `tempItem` VALUES (?,?,?,?)");
        pst2.setObject(1,cmbItemId.getValue());
        pst2.setObject(2,txtDescription.getText());
        pst2.setObject(3,txtQtyOnHand.getText());
        pst2.setObject(4,txtUnitPrice.getText());
        if(pst2.executeUpdate()>0){
            System.out.println("data inserted successfully");
        }else{
            System.out.println("error");
        }

    }

    public void addToCartOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
            String description = txtDescription.getText();
            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int qtyForCustomer = Integer.parseInt(txtOrderQty.getText());
            double total = qtyForCustomer*unitPrice;

            if (qtyOnHand<qtyForCustomer){
                new Alert(Alert.AlertType.WARNING,"Stock Quantity exceeded").show();
                return;
            }
            Button btn=new Button("Clear");
            Button qtyUp=new Button("+");
            Button qtyDown=new Button("-");

            CartTm tm = new CartTm(
                    cmbItemId.getValue(),
                    description,
                    qtyForCustomer,
                    unitPrice,
                    total,
                    btn,
                    qtyUp,
                    qtyDown
            );

            int rowNumber=isExists(tm);
            if (rowNumber==-1){
                obList.add(tm);
                btn.setOnAction((e)->{
                    obList.remove(tm);
                    try {
                        setItemData(tm.getCode(),1,txtDescription,txtUnitPrice,txtQtyOnHand,cmbItemId);
                        updateTempItemTableOfDb(1,0,tm);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                });
                qtyUp.setOnAction((e)->{

                    tm.setQty(tm.getQty()+2);
                    tm.setTotal(tm.getTotal()+ tm.getUnitPrice()*2);
                    calculateCost();
                    tblCart.refresh();
                    try {
                        setItemData(tm.getCode(),1,txtDescription,txtUnitPrice,txtQtyOnHand,cmbItemId);  // x variable just for method overloading
                        updateTempItemTableOfDb(3,qtyForCustomer,tm);
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
                    try {
                        setItemData(tm.getCode(),1,txtDescription,txtUnitPrice,txtQtyOnHand,cmbItemId);
                        updateTempItemTableOfDb(2,0,tm);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                });
                updateTempItemTableOfDb(0,qtyForCustomer,tm);   //Update The item table of DB
                resetItems();

            }else{
                CartTm temp = obList.get(rowNumber);
                temp.setQty(temp.getQty()+qtyForCustomer);
                temp.setTotal(temp.getTotal()+total);
                /*CartTm newTm = new CartTm(
                        temp.getCode(),
                        temp.getDescription(),
                        temp.getQty()+qtyForCustomer,
                        unitPrice,
                        total+temp.getTotal(),
                        btn,
                        qtyUp,
                        qtyDown
                );*/
                calculateCost();
                tblCart.refresh();
                updateTempItemTableOfDb(0,qtyForCustomer,temp);
                resetItems();
            }

            tblCart.setItems(obList);
            calculateCost();
    }

        private int isExists(CartTm tm){
            for (int i = 0; i < obList.size(); i++) {
                if (tm.getCode().equals(obList.get(i).getCode())){
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

    //---------------------------------------------------
    private void updateTempItemTableOfDb(int x, int qtyForCustomer , CartTm object ) throws SQLException, ClassNotFoundException {
        int newQty=0;
        if(x==0){
             newQty=Integer.parseInt(txtQtyOnHand.getText()) - qtyForCustomer;
        }
        if(x==1){
            newQty=Integer.parseInt(txtQtyOnHand.getText())+object.getQty();
        }
        if(x==2){
            newQty=Integer.parseInt(txtQtyOnHand.getText())+2;
        }
        if(x==3){
            newQty=Integer.parseInt(txtQtyOnHand.getText())-2;
        }
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("UPDATE `tempItem` set qtyOnHand=? where ItemCode=?");
        preparedStatement.setObject(1,newQty);
        preparedStatement.setObject(2,object.getCode());
        preparedStatement.executeUpdate();
        System.out.println(newQty);
        txtQtyOnHand.setText(String.valueOf(newQty));
        calculateCost();

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

    public void setItemData(String customerId,TextField txtDescription,TextField txtUnitPrice,TextField txtQtyOnHand,ComboBox<String> cmbItemId) throws SQLException, ClassNotFoundException {
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `tempItem`");
        ResultSet resultSet = pst.executeQuery();
        while(resultSet.next()){
            if(resultSet.getString(1).equals(customerId)){variable=1;break;}
        }
        if(variable==0){
            Item item1 = itemDAO.search(customerId);
            if (item1 == null) {
                new Alert(Alert.AlertType.WARNING, "Empty Result Set");
            } else {
                txtDescription.setText(item1.getDescription());
                txtUnitPrice.setText(String.valueOf(item1.getUnitPrice()));
                txtQtyOnHand.setText(String.valueOf(item1.getQtyOnHand()));
                cmbItemId.setValue(item1.getCode());
            }
        }else {
            setItemData(customerId,1,txtDescription,txtUnitPrice,txtQtyOnHand,cmbItemId);
        }
    }

    private void setItemData(String customerId,int x,TextField txtDescription,TextField txtUnitPrice,TextField txtQtyOnHand,ComboBox<String> cmbItemId) throws SQLException, ClassNotFoundException {
    // when we try to modify an Order, this method will call from there.

        Item item1 = null;
        PreparedStatement stm = DbConnection.getInstance()
                .getConnection().prepareStatement("SELECT * FROM `tempItem` WHERE ItemCode=?");
        stm.setObject(1, customerId);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            item1= new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4)
            );

        } else {
            System.out.println("Empty Result Set");
        }
        //--------------------------------

        if (item1 == null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set 1");
        } else {
            System.out.println(item1.getQtyOnHand());
            txtDescription.setText(item1.getDescription());
            txtUnitPrice.setText(String.valueOf(item1.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(item1.getQtyOnHand()));
            cmbItemId.setValue(item1.getCode());
        }
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
        if (new OrderDAOImpl().add(order)){
            DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `tempItem`").executeUpdate();
            tblCart.getItems().clear();
            lblOrderId.setText(new OrderDAOImpl().getOrderId());
            new Alert(Alert.AlertType.CONFIRMATION,"Order Placed..").show();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again..").show();
        }
    }

    public void cancelOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `tempItem`").executeUpdate();
        tblCart.getItems().clear();
        setItemData(cmbItemId.getValue(),txtDescription, txtUnitPrice, txtQtyOnHand, cmbItemId);
    }
}
