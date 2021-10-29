package controller;

import bo.BoFactory;
import bo.custom.ItemBO;
import bo.custom.ItemDeleteBO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import dto.ItemDTO;
import dto.tm.ItemTm;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeleteItemController {
    public TableView<ItemTm> tblItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colUnitPrice;
    ObservableList<ItemTm> items= FXCollections.observableArrayList();
    ItemTm x=null;

    /*Property injection has been injected*/
    private final ItemDeleteBO itemDelete = (ItemDeleteBO) BoFactory.getBoFactory().getBO(BoFactory.BoTypes.ITEM_DELETE);
    private final ItemBO itemBO= (ItemBO) BoFactory.getBoFactory().getBO(BoFactory.BoTypes.ITEM);

    public void initialize(){
        try {
            loadPreValues();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        colCode.setStyle("-fx-alignment: CENTER;");
        colDescription.setStyle("-fx-alignment: CENTER;");
        colQtyOnHand.setStyle("-fx-alignment: CENTER;");
        colUnitPrice.setStyle("-fx-alignment: CENTER;");
        colCode.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        tblItem.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    x= tblItem.getSelectionModel().getSelectedItem();
                });
    }

    public void deleteOnAction(ActionEvent actionEvent){
        try{
            itemDelete.deleteItem(x,items);
            tblItem.refresh();
        }catch (NullPointerException | SQLException | ClassNotFoundException e){
            new Alert(Alert.AlertType.WARNING,"Please select a item from `Item` table..").show();
        }
    }

    public void loadPreValues() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> allItemDTOS =  itemBO.getAllItems();
        for (ItemDTO a : allItemDTOS) {
            items.add(new ItemTm(a.getCode(),a.getDescription(),a.getQtyOnHand(),a.getUnitPrice()));
        }
        tblItem.setItems(items);
    }
}
