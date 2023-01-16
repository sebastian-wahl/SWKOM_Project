package at.fhtw.swen3.controller.rest;


import at.fhtw.swen3.controller.WarehouseApi;
import at.fhtw.swen3.persistence.entities.WarehouseEntity;
import at.fhtw.swen3.services.WarehouseService;
import at.fhtw.swen3.services.dto.Hop;
import at.fhtw.swen3.services.dto.Warehouse;
import at.fhtw.swen3.services.mapper.HopMapper;
import at.fhtw.swen3.services.mapper.JpaWarehouseContext;
import at.fhtw.swen3.services.mapper.WarehouseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Generated;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-13T12:19:08.753753Z[Etc/UTC]")
@Controller
@Slf4j
public class WarehouseApiController implements WarehouseApi {

    private final NativeWebRequest request;
    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    public WarehouseApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Warehouse> exportWarehouses() {
        return ResponseEntity.of(warehouseService.exportWarehouses().map(WarehouseMapper.INSTANCE::toDto));
    }

    @Override
    public ResponseEntity<Hop> getWarehouse(String code) {
        return ResponseEntity.of(warehouseService.getWarehouse(code).map(HopMapper.INSTANCE::toDto));
    }

    @Override
    public ResponseEntity<Void> importWarehouses(Warehouse warehouseDto) {
        WarehouseEntity warehouse = WarehouseMapper.INSTANCE.fromDto(warehouseDto, new JpaWarehouseContext());
        warehouseService.importWarehouses(warehouse);
        return ResponseEntity.ok(null);
    }
}
