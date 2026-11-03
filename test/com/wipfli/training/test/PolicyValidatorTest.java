package com.wipfli.training.test;

import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.service.PolicyValidator;
import org.junit.jupiter.api.Test;

import static com.wipfli.training.test.TestData.REFERENCE_DATE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PolicyValidatorTest {

    private final PolicyValidator validator = new PolicyValidator();

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

