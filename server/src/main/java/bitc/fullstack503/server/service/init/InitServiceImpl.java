package bitc.fullstack503.server.service.init;

import bitc.fullstack503.server.dto.station.SItemDTO;
import bitc.fullstack503.server.dto.station.StationDTO;
import bitc.fullstack503.server.mapper.init.InitMapper;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class InitServiceImpl implements InitService {
    @Value("${team3.station.service.url}")
    private String stationUrl;

    @Value("${team3.station.service.userKey}")
    private String stationUserKey;


    // 이부분 설명해주세요
    @PostConstruct
    public void init() throws Exception {
        String serviceKey = "?serviceKey=";
        String opt1 = "&pageNo=";
        String opt2 = "&numOfRows=";
        String ResultJson = "&resultType=json";
        String StationUrl = stationUrl + serviceKey + stationUserKey + opt1 + "1" + opt2 + "197" + ResultJson;


        System.out.println("지하철 역 간 거리 URL : " + StationUrl);

    }

    //getStationList(StationUrl);

    @Autowired
    private InitMapper initMapper;

    @Override
    public void getStationList (String url) throws Exception {

        List<SItemDTO> stationList;

        URL Serviceurl = null;
        HttpURLConnection UrlCon = null;
        BufferedReader reader = null;

        try {
            Serviceurl = new URL(url);
            UrlCon = (HttpURLConnection) Serviceurl.openConnection();
            UrlCon.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(UrlCon.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Gson gson = new Gson();

            StationDTO stationDTOList = gson.fromJson(sb.toString(), StationDTO.class);

            stationList = stationDTOList.getResponse().getBody().getItem();

            initMapper.insertStationList(stationList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
