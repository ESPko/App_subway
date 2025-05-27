package bitc.fullstack503.server.mapper;

import bitc.fullstack503.server.dto.mysql.*;
import bitc.fullstack503.server.dto.mysql.station_down.DSItemDTO;
import bitc.fullstack503.server.dto.mysql.station_up.USItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StationMapper {

    List<USItemDTO> getStationDistanceList(String startSc, String endSc) throws Exception;
    int getTimeTotalUp(int stStationInt, int edStationInt) throws Exception;
    int getTimeTotalDown(int stStationInt, int edStationInt) throws Exception;

    int getExchangeUp(int stStationInt, int edStationInt) throws Exception;
    int getExchangeDown(int stStationInt, int edStationInt) throws Exception;

    List<StationInfoDTO> getStationInfoList(String scode) throws Exception;

    List<TimeUPDTO> getTimeUpList(String scode) throws Exception;

    List<TimeDownDTO> getTimeDownList(String scode) throws Exception;
}
