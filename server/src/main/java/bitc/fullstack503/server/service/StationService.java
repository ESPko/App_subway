package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.mysql.StationInfoDTO;
import bitc.fullstack503.server.dto.mysql.station_up.USItemDTO;

import java.util.List;

public interface StationService {

    // 역정보
    List<StationInfoDTO> getstationInfoList() throws Exception;


   int getTimeTotalUp(String stStation, String edStation) throws Exception;
   int getTimeTotalDown(String stStation, String edStation) throws Exception;

    int getExchangeUp(String stStation, String edStation) throws Exception;
    int getExchangeDown(String stStation, String edStation) throws Exception;



}


