package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.api.station.SItemDTO;
import bitc.fullstack503.server.dto.api.station.StationDTO;
import bitc.fullstack503.server.dto.api.train.TItemDTO;
import bitc.fullstack503.server.dto.api.train.TrainDTO;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiserviceImpl implements Apiservice {
    @Override
    public List<SItemDTO> getStationJson(String url) throws Exception {

        List<SItemDTO> StationList = new ArrayList<>();

        URL Serviceurl = null;
        HttpURLConnection UrlCon = null;
        BufferedReader reader = null;

        try{
            Serviceurl = new URL(url);
            UrlCon = (HttpURLConnection) Serviceurl.openConnection();
            UrlCon.setRequestMethod("GET");

            String contentType = UrlCon.getHeaderField("Content-Type");
            String encoding = "UTF-8";  // 기본값은 UTF-8

            if (contentType != null && contentType.contains("charset=")) {
                encoding = contentType.substring(contentType.indexOf("charset=") + 8);
            }

            reader = new BufferedReader(new InputStreamReader(UrlCon.getInputStream(), encoding));


            StringBuilder sb = new StringBuilder();
            String line;



            while((line = reader.readLine()) != null){
                if(line.contains("<!DOCTYPE")) {
                    return null;
                }
                sb.append(line);
            }

//            System.out.println("응답 데이터: " + sb.toString());
            Gson gson = new Gson();



            StationDTO StationJson = gson.fromJson(sb.toString(), StationDTO.class);

            StationList = StationJson.getResponse().getBody().getItem();

        }catch (Exception e){
            e.printStackTrace();
        }


        return StationList;
    }

    @Override
    public List<TItemDTO> getTrainJson(String url) {
        List<TItemDTO> TrainList = new ArrayList<>();

        URL Serviceurl = null;
        HttpURLConnection UrlCon = null;
        BufferedReader reader = null;

        try{
            Serviceurl = new URL(url);
            UrlCon = (HttpURLConnection) Serviceurl.openConnection();
            UrlCon.setRequestMethod("GET");

            String contentType = UrlCon.getHeaderField("Content-Type");
            String encoding = "UTF-8";  // 기본값은 UTF-8

            if (contentType != null && contentType.contains("charset=")) {
                encoding = contentType.substring(contentType.indexOf("charset=") + 8);
            }

            reader = new BufferedReader(new InputStreamReader(UrlCon.getInputStream(), encoding));


            StringBuilder sb = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                if(line.contains("<!DOCTYPE")) {
                    return null;
                }
                sb.append(line);
            }
            Gson gson = new Gson();

            TrainDTO TrainJson = gson.fromJson(sb.toString(), TrainDTO.class);

            TrainList = TrainJson.getResponse().getBody().getItem();

        }catch (Exception e){
            e.printStackTrace();
        }



        return TrainList;
    }
}
