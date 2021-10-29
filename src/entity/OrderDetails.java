package entity;

public class OrderDetails {
    private String CustId;
    private String CustName;
    private String CustAddress;
    private String City;
    private String Province;
    private String PostalCode;

    public OrderDetails() {
    }

    public OrderDetails(String custId, String custName, String custAddress, String city, String province, String postalCode) {
        setCustId(custId);
        setCustName(custName);
        setCustAddress(custAddress);
        setCity(city);
        setProvince(province);
        setPostalCode(postalCode);
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getCustAddress() {
        return CustAddress;
    }

    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "CustId='" + CustId + '\'' +
                ", CustName='" + CustName + '\'' +
                ", CustAddress='" + CustAddress + '\'' +
                ", City='" + City + '\'' +
                ", Province='" + Province + '\'' +
                ", PostalCode='" + PostalCode + '\'' +
                '}';
    }
}
