package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.api.station.SItemDTO;
import bitc.fullstack503.server.dto.api.train.TItemDTO;

import java.util.List;

public interface Apiservice {
    List<SItemDTO> getStationJson(String url) throws Exception;
    List<TItemDTO> getTrainJson(String url) throws Exception;



}
