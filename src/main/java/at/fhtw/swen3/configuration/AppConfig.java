package at.fhtw.swen3.configuration;

import at.fhtw.swen3.gps.service.GeoEncodingService;
import at.fhtw.swen3.gps.service.impl.OpenStreetMapsEncodingProxy;
import at.fhtw.swen3.persistence.repositories.HopRepository;
import at.fhtw.swen3.persistence.repositories.ParcelRepository;
import at.fhtw.swen3.persistence.repositories.WarehouseRepository;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.WarehouseService;
import at.fhtw.swen3.services.impl.ParcelServiceImpl;
import at.fhtw.swen3.services.impl.WarehouseServiceImpl;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@TestConfiguration
public class AppConfig {

    @Bean
    public ParcelService parcelService(EntityValidatorService entityValidatorService, ParcelRepository parcelRepository, HopRepository hopRepository) {
        return new ParcelServiceImpl(entityValidatorService, parcelRepository, hopRepository);
    }

    @Bean
    public WarehouseService warehouseService(EntityValidatorService entityValidatorService, WarehouseRepository warehouseRepository, HopRepository hopRepository) {
        return new WarehouseServiceImpl(entityValidatorService, warehouseRepository, hopRepository);
    }

    @Bean
    public GeoEncodingService geoEncodingService() {
        return new OpenStreetMapsEncodingProxy(new RestTemplate());
    }
}
