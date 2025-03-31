package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.mysql.StationInfoDTO;
import bitc.fullstack503.server.dto.mysql.StationSheetDTO;
import bitc.fullstack503.server.dto.mysql.TimeDownDTO;
import bitc.fullstack503.server.dto.mysql.TimeUPDTO;
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
    public List<StationInfoDTO> getStationInfoList(String scode) throws Exception {
        return stationMapper.getStationInfoList(scode); // 지하철역 편의시설 정보
    }


    @Override
    public int getTimeTotalUp(int stStationInt, int edStationInt) throws Exception {
        return stationMapper.getTimeTotalUp(stStationInt, edStationInt);
    }

    @Override
    public int getTimeTotalDown(int stStationInt, int edStationInt) throws Exception {
        return stationMapper.getTimeTotalDown(stStationInt, edStationInt);
    }

    @Override
    public int getExchangeUp(int stStationInt, int edStationInt) throws Exception {
        return stationMapper.getExchangeUp(stStationInt, edStationInt);
    }

    @Override
    public int getExchangeDown(int stStationInt, int edStationInt) throws Exception {
        return stationMapper.getExchangeDown(stStationInt, edStationInt);
    }

    @Override
    public List<TimeUPDTO> getTimeUpList(String scode) throws Exception {
        return stationMapper.getTimeUpList(scode);
    }

    @Override
    public List<TimeDownDTO> getTimeDownList(String scode) throws Exception {
        return stationMapper.getTimeDownList(scode);
    }


}


