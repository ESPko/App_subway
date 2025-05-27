package bitc.fullstack503.server.mapper.init;

import bitc.fullstack503.server.dto.mysql.station_up.USItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InitMapper {

    void insertStationList(List<USItemDTO> stationList) throws Exception;
}
