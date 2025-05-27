package bitc.fullstack503.server.service.init;

import bitc.fullstack503.server.dto.mysql.station_up.USItemDTO;
import bitc.fullstack503.server.dto.mysql.station_up.UStationDTO;
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

    @Value("${team3.station.service.key}")
    private String stationUserKey;


    // 이부분 설명해주세요
    @PostConstruct
    public void init() throws Exception {
        String serviceKey = "?serviceKey=";
        String opt2 = "&numOfRows=328";
        String ResultJson = "&act=json";
        String StationUrl = stationUrl + serviceKey + stationUserKey + opt2 + ResultJson;


        System.out.println("지하철 역 간 거리 URL : " + StationUrl);

//        getStationList(StationUrl);
    }



    @Autowired
    private InitMapper initMapper;

    @Override
    public void getStationList (String url) throws Exception {

        List<USItemDTO> stationList;

        URL Serviceurl = null;
        HttpURLConnection UrlCon = null;
        BufferedReader reader = null;

        try {
            Serviceurl = new URL(url);
            UrlCon = (HttpURLConnection) Serviceurl.openConnection();
            UrlCon.setRequestMethod("GET");


            String contentType = UrlCon.getHeaderField("Content-Type");
            String encoding = "UTF-8";

            if (contentType != null && contentType.contains("charset=")) {
                encoding = contentType.substring(contentType.indexOf("charset=") + 8);
            }

            reader = new BufferedReader(new InputStreamReader(UrlCon.getInputStream(), encoding));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Gson gson = new Gson();

            UStationDTO stationDTOList = gson.fromJson(sb.toString(), UStationDTO.class);

            stationList = stationDTOList.getResponse().getBody().getItem();

            initMapper.insertStationList(stationList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
