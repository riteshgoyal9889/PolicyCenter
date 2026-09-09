package hashTest;

import java.util.Objects;

public class PolicyWithOverride {

    private String policyNumber;
    private String customerName;

    public PolicyWithOverride(String policyNumber, String customerName) {
        this.policyNumber = policyNumber;
        this.customerName = customerName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PolicyWithOverride other = (PolicyWithOverride) obj;

        return Objects.equals(policyNumber, other.policyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policyNumber);
    }
}