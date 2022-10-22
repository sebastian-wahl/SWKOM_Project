package at.fhtw.swen3.services.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import javax.annotation.Generated;

/**
 * WarehouseAllOfNextHops
 */

@JsonTypeName("warehouse_allOf_nextHops")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-13T12:19:08.753753Z[Etc/UTC]")
public class WarehouseNextHopsDto {

  @JsonProperty("traveltimeMins")
  private Integer traveltimeMins;

  @JsonProperty("hop")
  private HopDto hopDto;

  public WarehouseNextHopsDto traveltimeMins(Integer traveltimeMins) {
    this.traveltimeMins = traveltimeMins;
    return this;
  }

  /**
   * Get traveltimeMins
   * @return traveltimeMins
  */
  @NotNull 
  @Schema(name = "traveltimeMins", required = true)
  public Integer getTraveltimeMins() {
    return traveltimeMins;
  }

  public void setTraveltimeMins(Integer traveltimeMins) {
    this.traveltimeMins = traveltimeMins;
  }

  public WarehouseNextHopsDto hop(HopDto hopDto) {
    this.hopDto = hopDto;
    return this;
  }

  /**
   * Get hop
   * @return hop
  */
  @NotNull @Valid 
  @Schema(name = "hop", required = true)
  public HopDto getHop() {
    return hopDto;
  }

  public void setHop(HopDto hopDto) {
    this.hopDto = hopDto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarehouseNextHopsDto warehouseNextHopsDto = (WarehouseNextHopsDto) o;
    return Objects.equals(this.traveltimeMins, warehouseNextHopsDto.traveltimeMins) &&
        Objects.equals(this.hopDto, warehouseNextHopsDto.hopDto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(traveltimeMins, hopDto);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarehouseAllOfNextHops {\n");
    sb.append("    traveltimeMins: ").append(toIndentedString(traveltimeMins)).append("\n");
    sb.append("    hop: ").append(toIndentedString(hopDto)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

