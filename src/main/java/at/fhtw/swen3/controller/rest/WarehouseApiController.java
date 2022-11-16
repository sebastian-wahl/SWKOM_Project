package at.fhtw.swen3.controller.rest;


import at.fhtw.swen3.controller.WarehouseApi;
import at.fhtw.swen3.persistence.entities.WarehouseEntity;
import at.fhtw.swen3.services.WarehouseService;
import at.fhtw.swen3.services.dto.Hop;
import at.fhtw.swen3.services.dto.Warehouse;
import at.fhtw.swen3.services.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Generated;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-13T12:19:08.753753Z[Etc/UTC]")
@Controller
public class WarehouseApiController implements WarehouseApi {

    @Autowired
    private WarehouseService warehouseService;

    private final NativeWebRequest request;

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
        Warehouse warehouse = warehouseService.exportWarehouses();
        return ResponseEntity.of(Optional.ofNullable(warehouse));
    }

    @Override
    public ResponseEntity<Hop> getWarehouse(String code) {
        Hop hop = warehouseService.getWarehouse(code);
        return ResponseEntity.of(Optional.ofNullable(hop));
    }

    @Override
    public ResponseEntity<Void> importWarehouses(Warehouse warehouseDto) {
        WarehouseEntity warehouse = WarehouseMapper.INSTANCE.fromDto(warehouseDto);
        warehouseService.importWarehouses(warehouse);
        return ResponseEntity.ok(null);
    }
}
