package com.wipfli.insurancemanagement.test;

import com.wipfli.insurancemanagement.exception.InvalidPolicyDataException;
import com.wipfli.insurancemanagement.service.PolicyValidator;
import org.junit.jupiter.api.Test;


import static com.wipfli.insurancemanagement.test.TestData.REFERENCE_DATE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PolicyValidatorTest {

    private final PolicyValidator validator = new PolicyValidator();
/*

a)for name
    valid customer name passes validation successfully
    Empty customer name throws InvalidPolicyDataException
    Customer name containing digits throws InvalidPolicyDataException

b)for age
    Valid customer age passes validation successfully
    Underage customer throws InvalidPolicyDataException
    Overage customer throws InvalidPolicyDataException

c)for policynumber
    Valid policy number passes validation successfully
    Empty policy number throws InvalidPolicyDataException

d)for claim count
    Negative claim count throws InvalidPolicyDataException

e)for expiry dATE
    Past expiry date throws
 */

    @Test
    void validCustomerNamePasses() {
        assertDoesNotThrow(() -> validator.validateCustomerName("Ravi Kumar"));
    }
    @Test
    void emptyCustomerNameThrowsInvalidPolicyDataException() {
        InvalidPolicyDataException exception = assertThrows(
                InvalidPolicyDataException.class,
                () -> validator.validateCustomerName("   "));
        assertEquals("UNKNOWN", exception.getPolicyNumber());
    }
    @Test
    void customerNameWithDigitsThrowsInvalidPolicyDataException() {
        assertThrows(
                InvalidPolicyDataException.class,
                () -> validator.validateCustomerName("Ravi123"));
    }

    @Test
    void validCustomerAgePasses() {
        assertDoesNotThrow(() -> validator.validateCustomerAge(25));
    }

    @Test
    void underageCustomerThrowsInvalidPolicyDataException() {
        assertThrows(
                InvalidPolicyDataException.class,
                () -> validator.validateCustomerAge(17));
    }

    @Test
    void overageCustomerThrowsInvalidPolicyDataException() {
        assertThrows(InvalidPolicyDataException.class,
                () -> validator.validateCustomerAge(101));
    }

    @Test
    void validPolicyNumberPasses() {
        assertDoesNotThrow(() -> validator.validatePolicyNumber("POL-2001"));
    }

    @Test
    void emptyPolicyNumberThrowsInvalidPolicyDataException() {
        assertThrows(InvalidPolicyDataException.class, () -> validator.validatePolicyNumber("  "));
    }

    @Test
    void negativeClaimCountThrowsInvalidPolicyDataException() {
        InvalidPolicyDataException exception = assertThrows(
                InvalidPolicyDataException.class,
                () -> validator.validateClaimCount("POL-2001", -1));
        assertEquals("POL-2001", exception.getPolicyNumber());
    }

    @Test
    void pastExpiryDateThrowsInvalidPolicyDataException() {
        assertThrows(InvalidPolicyDataException.class,
                () -> validator.validateExpiryDate("POL-2001", REFERENCE_DATE.minusYears(2), 1));
    }
}
