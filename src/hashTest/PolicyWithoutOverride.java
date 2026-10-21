package hashTest;

public class PolicyWithoutOverride {

    private String policyNumber;
    private String customerName;

    public PolicyWithoutOverride(String policyNumber, String customerName) {
        this.policyNumber = policyNumber;
        this.customerName = customerName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public String getCustomerName() {
        return customerName;
    }
}