package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.station.SItemDTO;
import bitc.fullstack503.server.dto.station.StationDTO;
import bitc.fullstack503.server.dto.train.TItemDTO;

import java.util.List;

public interface Apiservice {
    List<SItemDTO> getStationJson(String url) throws Exception;

    List<TItemDTO> getTrainJson(String url) throws Exception;

}
