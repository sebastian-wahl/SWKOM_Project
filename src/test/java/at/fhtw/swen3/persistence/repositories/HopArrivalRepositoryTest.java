package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.HopArrivalEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HopArrivalRepositoryTest {
    private static final String HOP_CODE = "ABCD12";
    private static final String HOP_DESC = "Test";
    @Autowired
    private HopArrivalRepository hopArrivalRepository;

    @Test
    void GIVEN_correct_trackingId_WHEN_findFirstByTrackingId_RETURN_parcel_with_hop_arrival() {
        // GIVEN
        OffsetDateTime dateNow = OffsetDateTime.now();

        HopArrivalEntity hopArrival = HopArrivalEntity.builder()
                .dateTime(dateNow)
                .code(HOP_CODE)
                .description(HOP_DESC)
                .build();

        hopArrivalRepository.save(hopArrival);

        // WHEN
        var optionalHop = hopArrivalRepository.findFirstByCode(HOP_CODE);

        // THEN
        assertThat(optionalHop).isPresent();
        assertThat(optionalHop.get().getDescription()).isEqualTo(HOP_DESC);
        assertThat(optionalHop.get().getCode()).isEqualTo(HOP_CODE);
        // ToDO fix date assertion
    }
}