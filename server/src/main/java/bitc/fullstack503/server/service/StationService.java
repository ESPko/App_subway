package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.mysql.StationInfoDTO;
import bitc.fullstack503.server.dto.mysql.StationSheetDTO;
import bitc.fullstack503.server.dto.mysql.TimeDownDTO;
import bitc.fullstack503.server.dto.mysql.TimeUPDTO;
import bitc.fullstack503.server.dto.mysql.station_up.USItemDTO;

import java.util.List;

public interface StationService {

    // 역정보
    List<StationInfoDTO> getStationInfoList(String scode) throws Exception;


   int getTimeTotalUp(int stStationInt, int edStationInt) throws Exception;
   int getTimeTotalDown(int stStationInt, int edStationInt) throws Exception;

    int getExchangeUp(int stStationInt, int edStationInt) throws Exception;
    int getExchangeDown(int stStationInt, int edStationInt) throws Exception;



    List<TimeUPDTO> getTimeUpList(String scode) throws Exception;

    List<TimeDownDTO> getTimeDownList(String scode) throws Exception;
}


