package bo;

import bo.custom.UpdateItemBO;
import bo.custom.impl.*;

public class BoFactory {
    private static BoFactory boFactory;

    private BoFactory(){}

    public static BoFactory getBoFactory(){
        if (boFactory==null){
            boFactory=new BoFactory();
        }
        return boFactory;
    }

    public SuperBO getBO(BoTypes types) {
        switch (types) {
            case ITEM:
                return new ItemBOImpl();
            case CUSTOMER:
                return new CustomerBOImpl();
            case PURCHASE_ORDER:
                return new PurchaseOrderBOImpl();
            case ITEM_DELETE:
                return new ItemDeleteBOImpl();
            case ORDER_DETAIL:
                return new OrderDetailBOImpl();
            case UPDATE_ORDER:
                return new UpdateOrderBOImpl();
                case UPDATE_ITEM:
                return new UpdateItemBOImpl();
            default:
                return null;
        }
    }

    public enum BoTypes {
        CUSTOMER, ITEM, PURCHASE_ORDER,ITEM_DELETE,ORDER_DETAIL,UPDATE_ORDER,UPDATE_ITEM
    }

}
