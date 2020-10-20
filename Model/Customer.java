package Model;

public class Customer {

    private int customerId;
    private String customerName;
    private String addressId;
    private int active;
    private String phone;

    public Customer(int customerId, String customerName, String addressId, int active, String phone) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
        this.phone = phone;
    }

    public Customer(){

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }


    public String getPhone() {
        return phone; //temp
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
