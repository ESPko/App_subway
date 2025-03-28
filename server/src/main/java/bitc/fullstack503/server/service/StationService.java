package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.mysql.StationInfoDTO;
import bitc.fullstack503.server.dto.mysql.station_up.USItemDTO;

import java.util.List;

public interface StationService {

    // 역정보
    List<StationInfoDTO> getStationInfoList(String scode) throws Exception;

    List<USItemDTO> getStationDistanceList(String stStation, String edStation) throws Exception;

   int getDistanceTotal(String stStation, String edStation) throws Exception;

   int getTimeTotal(String stStation, String edStation) throws Exception;




}


