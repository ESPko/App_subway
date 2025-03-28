package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.mysql.StationInfoDTO;
import bitc.fullstack503.server.dto.mysql.station_up.USItemDTO;
import bitc.fullstack503.server.mapper.StationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationMapper stationMapper;

    @Override
    public List<USItemDTO> getStationDistanceList(String startSc, String endSc) throws Exception {
        return stationMapper.getStationDistanceList(startSc, endSc);
    }


    @Override
    public List<StationInfoDTO> getStationInfoList(String scode) throws Exception {
        return stationMapper.getStationInfoList(scode); // 지하철역 편의시설 정보
    }

    @Override
    public int getDistanceTotal(String stStation, String edStation) throws Exception {
        return stationMapper.getDistanceTotal(stStation, edStation);

    }

    @Override
    public int getTimeTotal(String stStation, String edStation) throws Exception {
        return stationMapper.getTimeTotal(stStation, edStation);


    }
}







