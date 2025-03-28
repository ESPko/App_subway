package bitc.fullstack503.server.mapper;

import bitc.fullstack503.server.dto.mysql.CategoryDTO;
import bitc.fullstack503.server.dto.mysql.StationInfoDTO;
import bitc.fullstack503.server.dto.mysql.station_down.DSItemDTO;
import bitc.fullstack503.server.dto.mysql.station_up.USItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StationMapper {

    List<USItemDTO> getStationDistanceList(String startSc, String endSc) throws Exception;
    int getTimeTotalUp(String stStation, String edStation) throws Exception;
    int getTimeTotalDown(String stStation, String edStation) throws Exception;

    int getExchangeUp(String stStation, String edStation) throws Exception;
    int getExchangeDown(String stStation, String edStation) throws Exception;

    List<StationInfoDTO> getStationInfoList(String scode) throws Exception;













}
