package controller;

import bo.BoFactory;
import bo.custom.UpdateItemBO;
import com.jfoenix.controls.JFXTextField;
import dao.custom.impl.ItemDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import dto.ItemDTO;
import dto.tm.ItemTm;

import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateItemController {

    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public Label lblPreUnitPrice;
    public Label lblPreQtyOnHand;
    public Label lblPreDes;
    public Label lblPreId;
    public TableView<ItemTm> tblItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    ObservableList<ItemTm> items= FXCollections.observableArrayList();

    /*property injection has been injected*/
    private final UpdateItemBO updateItemBO = (UpdateItemBO) BoFactory.getBoFactory().getBO(BoFactory.BoTypes.UPDATE_ITEM);

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
        colCode.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));

        tblItem.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    ItemTm x=tblItem.getSelectionModel().getSelectedItem();
                    try {
                        setPreValues(x);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });

    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (lblPreId.getText().equals("") ){new Alert(Alert.AlertType.WARNING,"Please select a row from `Item` table..").show();}
        else {
            if (txtDescription.getText().equals("")){new Alert(Alert.AlertType.WARNING,"Some fields may be empty..").show();}
            else{   new ItemDAOImpl().update(new ItemDTO(lblPreId.getText(),txtDescription.getText(),Integer.parseInt(txtQtyOnHand.getText())
                    ,Integer.parseInt(txtUnitPrice.getText())));
                txtQtyOnHand.clear();
                txtUnitPrice.clear();
                lblPreUnitPrice.setText("--");
                lblPreQtyOnHand.setText("--");
            }
        }
    }

    public void loadPreValues() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> allItemDTOS = updateItemBO.getAllItems();
        for (ItemDTO allItemDTO : allItemDTOS) {
            items.add(new ItemTm(allItemDTO.getCode(), allItemDTO.getDescription()));
        }
        tblItem.setItems(items);
    }

    public void setPreValues(ItemTm x) throws SQLException, ClassNotFoundException {
        lblPreId.setText(updateItemBO.searchForItem(x.getId()).getCode());
        lblPreDes.setText(updateItemBO.searchForItem(x.getId()).getDescription());
        lblPreQtyOnHand.setText(String.valueOf(updateItemBO.searchForItem(x.getId()).getQtyOnHand()));
        lblPreUnitPrice.setText(String.valueOf(updateItemBO.searchForItem(x.getId()).getUnitPrice()));
        txtDescription.setText(updateItemBO.searchForItem(x.getId()).getDescription());
    }
}
