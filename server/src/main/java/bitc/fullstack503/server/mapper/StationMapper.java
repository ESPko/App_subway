package bitc.fullstack503.server.mapper;

import bitc.fullstack503.server.dto.mysql.StationInfoDTO;
import bitc.fullstack503.server.dto.mysql.station_up.USItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StationMapper {

    List<USItemDTO> getStationDistanceList(String startSc, String endSc) throws Exception;

    List<StationInfoDTO> getStationInfoList(String scode) throws Exception;

    int getDistanceTotal(String stStation, String edStation) throws Exception ;

    int getTimeTotal(String stStation, String edStation) throws Exception ;












}
