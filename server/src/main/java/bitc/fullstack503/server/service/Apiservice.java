package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.station_up.SItemDTO;

import java.util.List;

public interface Apiservice {
    List<SItemDTO> getStationJson(String url) throws Exception;
}
