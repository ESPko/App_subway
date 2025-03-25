package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.station.SItemDTO;
import bitc.fullstack503.server.dto.station.StationDTO;

import java.util.List;

public interface Apiservice {
    List<SItemDTO> getStationJson(String url) throws Exception;
}
