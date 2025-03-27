package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.mysql.StationInfoDTO;
import bitc.fullstack503.server.dto.station_up.SItemDTO;

import java.util.List;

public interface StationService {

    // 역정보
    List<StationInfoDTO> getstationInfoList() throws Exception;


    List<SItemDTO> getStationDistanceList(String stStation, String edStation) throws Exception;

   int getDistanceTotal(String stStation, String edStation) throws Exception;

   int getTimeTotal(String stStation, String edStation) throws Exception;




}


