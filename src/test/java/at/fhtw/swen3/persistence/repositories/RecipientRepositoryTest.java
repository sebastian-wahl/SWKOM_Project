package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import at.fhtw.swen3.persistence.entities.RecipientEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RecipientRepositoryTest {

    @Autowired
    private RecipientRepository recipientRepository;

    @AfterEach
    void tearDown() {
        //recipientRepository.deleteAll();
    }

    @Test
    void GIVEN_correct_trackingId_WHEN_findFirstByTrackingId_RETURN_parcel() {

        RecipientEntity recipient = RecipientEntity.builder().name("Test Name").postalCode("A-1200").city("Wien").country("Österreich").street("Test Street 22/2").build();

        OffsetDateTime dateTime = OffsetDateTime.now();

        HopArrivalEntity hopArrival = HopArrivalEntity.builder()
                .code("TXXO5")
                .description("Test")
                .dateTime(dateTime)
                .build();

        recipientRepository.save(recipient);

        var optionalParcel = recipientRepository.findFirstByNameAndStreetAndCityAndCountry(recipient.getName(), recipient.getStreet(), recipient.getCity(), recipient.getCountry());

        assertThat(optionalParcel).isPresent();
        assertThat(optionalParcel.get().getName()).isEqualTo(recipient.getName());
        assertThat(optionalParcel.get().getPostalCode()).isEqualTo(recipient.getPostalCode());
        assertThat(optionalParcel.get().getCity()).isEqualTo(recipient.getCity());
        assertThat(optionalParcel.get().getCountry()).isEqualTo(recipient.getCountry());
    }
}