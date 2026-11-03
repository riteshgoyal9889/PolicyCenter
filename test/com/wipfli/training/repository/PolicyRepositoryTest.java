package com.wipfli.training.repository;

import com.wipfli.training.exception.IllegalStatusChangeException;
import com.wipfli.training.model.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PolicyRepositoryTest {

    @Autowired
    private PolicyRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void each_policy_type_is_saved_and_read_back_as_its_own_type() {
        PolicyHolder owner = new PolicyHolder("Ravi Kumar", 30);

        CarPolicy car = new CarPolicy("POL-1001", owner, VehicleType.CAR, 0,
                LocalDate.of(2026, 1, 1), 1, "KA-01-AB-1234");
        BikePolicy bike = new BikePolicy("POL-1002", owner, VehicleType.BIKE, 0,
                LocalDate.of(2026, 1, 1), 1, 150);
        TruckPolicy truck = new TruckPolicy("POL-1003", owner, VehicleType.TRUCK, 0,
                LocalDate.of(2026, 1, 1), 1, 12.5);

        repository.saveAll(List.of(car, bike, truck));
        entityManager.clear();

        Policy savedCar = repository.findById("POL-1001").orElseThrow();
        Policy savedBike = repository.findById("POL-1002").orElseThrow();
        Policy savedTruck = repository.findById("POL-1003").orElseThrow();

        assertInstanceOf(CarPolicy.class, savedCar);
        assertInstanceOf(BikePolicy.class, savedBike);
        assertInstanceOf(TruckPolicy.class, savedTruck);

        assertEquals("KA-01-AB-1234", ((CarPolicy) savedCar).getRegistrationNumber());
        assertEquals(150, ((BikePolicy) savedBike).getEngineCC());
        assertEquals(12.5, ((TruckPolicy) savedTruck).getLoadCapacityTons());
    }

    @Test
    void status_change_is_persisted_and_expired_policy_refuses_renew() {
        PolicyHolder owner = new PolicyHolder("Asha", 28);
        CarPolicy policy = new CarPolicy("POL-2001", owner, VehicleType.CAR, 0,
                LocalDate.of(2026, 1, 1), 2, "TN-09-XYZ-9999");

        repository.save(policy);
        entityManager.clear();

        Policy saved = repository.findById("POL-2001").orElseThrow();
        saved.expire();
        repository.save(saved);
        entityManager.clear();

        Policy reloaded = repository.findById("POL-2001").orElseThrow();
        assertEquals(PolicyStatus.EXPIRED, reloaded.getStatus());

        assertThrows(IllegalStatusChangeException.class,
                () -> reloaded.renew(LocalDate.of(2026, 8, 1)));
    }

    @Test
    void status_column_holds_text_not_number() {
        PolicyHolder owner = new PolicyHolder("Meera", 35);
        CarPolicy policy = new CarPolicy("POL-3001", owner, VehicleType.CAR, 0,
                LocalDate.of(2026, 1, 1), 2, "KL-12-CD-8888");

        repository.save(policy);

        String statusValue = jdbcTemplate.queryForObject(
                "select status from policy where policy_number = ?",
                String.class,
                "POL-3001");

        assertEquals("ACTIVE", statusValue);
    }

    @Test
    void customer_name_query_returns_exact_customer_policies() {
        PolicyHolder owner1 = new PolicyHolder("Ravi", 29);
        PolicyHolder owner2 = new PolicyHolder("Ravi", 31);

        repository.save(new CarPolicy("POL-4001", owner1, VehicleType.CAR, 0,
                LocalDate.of(2026, 1, 1), 1, "KA-11-AA-1111"));
        repository.save(new BikePolicy("POL-4002", owner2, VehicleType.BIKE, 0,
                LocalDate.of(2026, 1, 1), 1, 125));
        repository.save(new CarPolicy("POL-4003", owner1, VehicleType.CAR, 0,
                LocalDate.of(2026, 1, 1), 1, "KA-11-AA-2222"));

        List<Policy> result = repository.findByPolicyHolderNameIgnoreCase("ravi");

        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(p -> p.getPolicyHolder().getName().equalsIgnoreCase("Ravi")));
    }

    @Test
    void expiry_range_query_checks_both_boundary_dates() {
        PolicyHolder owner = new PolicyHolder("Neha", 32);

        repository.save(new CarPolicy("POL-5001", owner, VehicleType.CAR, 0,
                LocalDate.of(2026, 8, 8), 1, "DL-1-AAA-1111"));
        repository.save(new CarPolicy("POL-5002", owner, VehicleType.CAR, 0,
                LocalDate.of(2026, 8, 9), 1, "DL-1-AAA-2222"));
        repository.save(new CarPolicy("POL-5003", owner, VehicleType.CAR, 0,
                LocalDate.of(2026, 8, 30), 1, "DL-1-AAA-3333"));
        repository.save(new CarPolicy("POL-5004", owner, VehicleType.CAR, 0,
                LocalDate.of(2026, 9, 1), 1, "DL-1-AAA-4444"));

        List<Policy> result = repository.findByExpiryDateBetween(
                LocalDate.of(2026, 8, 9),
                LocalDate.of(2026, 8, 30)
        );

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getPolicyNumber().equals("POL-5002")));
        assertTrue(result.stream().anyMatch(p -> p.getPolicyNumber().equals("POL-5003")));
        assertFalse(result.stream().anyMatch(p -> p.getPolicyNumber().equals("POL-5001")));
        assertFalse(result.stream().anyMatch(p -> p.getPolicyNumber().equals("POL-5004")));
    }

    @Test
    void saving_same_policy_number_twice_directly_through_repository_throws_exception() {
        PolicyHolder owner = new PolicyHolder("Sonia", 40);

        repository.save(new CarPolicy("POL-6001", owner, VehicleType.CAR, 0,
                LocalDate.of(2026, 1, 1), 1, "MH-15-QQ-1111"));

        assertThrows(Exception.class, () -> repository.saveAndFlush(
                new CarPolicy("POL-6001", owner, VehicleType.CAR, 0,
                        LocalDate.of(2026, 1, 2), 1, "MH-15-QQ-2222")));
    }

    @Test
    void reading_back_an_object_just_saved_proves_it_comes_from_database_not_memory() {
        PolicyHolder owner = new PolicyHolder("Tarun", 27);

        CarPolicy original = new CarPolicy("POL-7001", owner, VehicleType.CAR, 0,
                LocalDate.of(2026, 1, 1), 2, "AP-05-XX-7777");

        repository.save(original);
        entityManager.clear();

        CarPolicy loaded = (CarPolicy) repository.findById("POL-7001").orElseThrow();

        assertNotSame(original, loaded);
        assertEquals("POL-7001", loaded.getPolicyNumber());
        assertEquals("AP-05-XX-7777", loaded.getRegistrationNumber());
    }
}

