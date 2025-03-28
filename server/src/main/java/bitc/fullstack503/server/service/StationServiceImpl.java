package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.mysql.StationInfoDTO;
import bitc.fullstack503.server.mapper.StationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationMapper stationMapper;

    @Override
    public List<StationInfoDTO> getstationInfoList() throws Exception {
        return stationMapper.getstationInfoList();
    }


    @Override
    public int getTimeTotalUp(String stStation, String edStation) throws Exception {
        return stationMapper.getTimeTotalUp(stStation, edStation);
    }

    @Override
    public int getTimeTotalDown(String stStation, String edStation) throws Exception {
        return stationMapper.getTimeTotalDown(stStation, edStation);
    }

    @Override
    public int getExchangeUp(String stStation, String edStation) throws Exception {
        return stationMapper.getExchangeUp(stStation, edStation);
    }

    @Override
    public int getExchangeDown(String stStation, String edStation) throws Exception {
        return stationMapper.getExchangeDown(stStation, edStation);
    }





}


