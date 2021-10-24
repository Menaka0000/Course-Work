package controller;

import dao.custom.impl.OrderDAOImpl;
import db.DbConnection;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    static int variable=0;

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
                        new NewOrderController().setItemData(newValue,txtDescription, txtUnitPrice, txtQtyOnHand, cmbItemId);
                        System.out.println(newValue);
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
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * From `order Detail`");
            ResultSet rst = stm.executeQuery();
            while(rst.next()){
                if (rst.getString(2).equals(txtOrderIdOrName.getText())){
                    PreparedStatement stm1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `item` WHERE ItemCode=?");
                    stm1.setObject(1,rst.getString(1));
                    ResultSet rst1 = stm1.executeQuery();
                    rst1.next();
                    Button btn=new Button("Clear");
                    Button qtyUp=new Button("+");
                    Button qtyDown=new Button("-");

                    CartTm tm=new CartTm(rst.getString(1),rst1.getString(2),rst.getInt(3),
                            rst1.getInt(4),rst.getInt(3)* rst1.getInt(4),btn,qtyUp,qtyDown);
                    btn.setOnAction((e)->{
                        obList1.remove(tm);
                        try {
                            setItemData(tm.getCode(),1);
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
                        tblOderItem.refresh();
                        try {
                            setItemData(tm.getCode(),1);  // x variable just for method overloading
                            int qtyForCustomer=0;
                            if(!txtOrderQty.getText().equals("")){
                                 qtyForCustomer = Integer.parseInt(txtOrderQty.getText());}
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
                        tblOderItem.refresh();
                        try {
                            setItemData(tm.getCode(),1);
                            updateTempItemTableOfDb(2,0,tm);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                    });
                    obList1.add(tm);
                    calculateCost();
                }
            }
            tblOderItem.setItems(obList1);
            resetItems(1);
            //System.out.println("There are some numbers.");
        }else {
            cmbOrderIds.getItems().clear();
            ArrayList<String> nameList=new ArrayList<>();
            List<String> ids=new ArrayList<>();
            Connection con=DbConnection.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM `customer` WHERE CustName=?");
            pst.setObject(1,txtOrderIdOrName.getText());
            ResultSet rst = pst.executeQuery();
            while (rst.next()){nameList.add(rst.getString(1));}
            for (String x:nameList
                 ) {
                PreparedStatement pst1 = con.prepareStatement("SELECT * FROM `order` WHERE cId=?");
                pst1.setObject(1,x);
                ResultSet rst1 = pst1.executeQuery();
                while (rst1.next()){ids.add(rst1.getString(1));}
            }
            System.out.println(ids);
            cmbOrderIds.getItems().addAll(ids);
            System.out.println(ids);
            //System.out.println("no numbers");
        }
    }

    public void addOnAction(ActionEvent actionEvent) {
        String description = txtDescription.getText();
        int qtyForCustomer = Integer.parseInt(txtOrderQty.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
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
            obList1.add(tm);
            btn.setOnAction((e)->{
                obList1.remove(tm);
                try {
                    setItemData(tm.getCode(),1);
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
                tblOderItem.refresh();
                try {
                    setItemData(tm.getCode(),1);  // x variable, just for method overloading
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
                tblOderItem.refresh();
                try {
                    setItemData(tm.getCode(),1);
                    updateTempItemTableOfDb(2,0,tm);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            });
            try {
                updateTempItemTableOfDb(0,qtyForCustomer,tm);//Update The item table of DB
                resetItems(0);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }else{
            CartTm temp = obList1.get(rowNumber);
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
            tblOderItem.refresh();
            try {
                updateTempItemTableOfDb(0,qtyForCustomer,temp);
                resetItems(0);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

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

    public void resetItems(int z) throws SQLException, ClassNotFoundException {
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
        }if(z==0){
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
        if(z==1){
            for (CartTm x:obList1
                 ) {
                PreparedStatement pst2 = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `tempItem` VALUES (?,?,?,?)");
                pst2.setObject(1,x.getCode());
                pst2.setObject(2,x.getDescription());
                PreparedStatement pst3 = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `item` WHERE ItemCode=?");
                pst3.setObject(1,x.getCode());
                ResultSet rst3 = pst3.executeQuery();
                rst3.next();
                pst2.setObject(3,rst3.getString(3));
                pst2.setObject(4,x.getUnitPrice());
                if(pst2.executeUpdate()>0){
                    System.out.println("data inserted successfully");
                }else{
                    System.out.println("error");
                }
            }

        }
    }

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

    private void setItemData(String customerId,int x) throws SQLException, ClassNotFoundException {
        variable=0;
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
        DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `tempItem`").executeUpdate();

    }

    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       new OrderDAOImpl().deleteOrder(obList1,txtOrderIdOrName.getText());
       DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `tempItem`").executeUpdate();

    }
}
