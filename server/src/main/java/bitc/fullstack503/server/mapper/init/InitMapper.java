package bitc.fullstack503.server.mapper.init;

import bitc.fullstack503.server.dto.station_up.SItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InitMapper {

    void insertStationList(List<SItemDTO> stationList) throws Exception;
}
