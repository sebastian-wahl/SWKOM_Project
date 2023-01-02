package at.fhtw.swen3.persistence.repositories;

import at.fhtw.swen3.persistence.entities.GeoCoordinateEntity;
import at.fhtw.swen3.persistence.entities.TransferwarehouseEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransferwarehouseRepositoryTest {

    @Autowired
    private TransferwarehouseRepository transferwarehouseRepository;

    @AfterEach
    void tearDown() {
        transferwarehouseRepository.deleteAll();
    }

    @Test
    void GIVEN_saved_transferwarehouseEntity_WHEN_findById_THEN_entity_found() {
        // GIVEN
        GeoCoordinateEntity geoCoordinateEntity = GeoCoordinateEntity.builder().build();

        TransferwarehouseEntity transferwarehouseEntity = TransferwarehouseEntity.builder()
                .code("ABCD1234")
                .hopType("warehouse")
                .description("description")
                .locationCoordinates(geoCoordinateEntity)
                .locationName("Vienna")
                .processingDelayMins(2)
                .regionGeoJson("regionGeoJson")
                .logisticsPartner("logisticPartner")
                .logisticsPartnerUrl("logisticPartnerUrl")
                .build();

        transferwarehouseRepository.save(transferwarehouseEntity);

        // WHEN
        Optional<TransferwarehouseEntity> foundTransferwarehouse = transferwarehouseRepository.findById(transferwarehouseEntity.getId());

        // THEN
        assertThat(foundTransferwarehouse).isPresent();
        assertThat(foundTransferwarehouse.get())
                .usingRecursiveComparison()
                .isEqualTo(transferwarehouseEntity);
    }
}