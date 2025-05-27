package bitc.fullstack503.server.appserver;

import bitc.fullstack503.server.dto.mysql.*;
import bitc.fullstack503.server.dto.api.train.TItemDTO;
import bitc.fullstack503.server.service.Apiservice;
import bitc.fullstack503.server.service.Categoryservice;
import bitc.fullstack503.server.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/app")
@RestController
public class JSYController {
    @Value("${team3.station.service.key}")
    private String stationServiceKey;

    @Value("${team3.station.service.url}")
    private String stationServiceUrl;


    @Value("${team3.train.service.url}")
    private String trainServiceUrl;

    @Value("${team3.train.service.key}")
    private String trainServiceKey;

    @Autowired
    private Apiservice apiservice;

    @Autowired
    private Categoryservice categoryservice;

    @Autowired
    private StationService stationservice;

    // select * from category where scode = ?
    @GetMapping("/app/stationListName/{scode}")
    public List<CategoryDTO> getStationListName(@PathVariable String scode) throws Exception {

        List<CategoryDTO> categoryList = categoryservice.getCategoryLineList(scode);

        return categoryList;
    }


    @GetMapping("/app/train/{scode}/{sttime}/{day}")
    public Map<String, List<Integer>> gettrain(@PathVariable String scode,@PathVariable String sttime,@PathVariable String day) throws Exception {

        int hour = Integer.parseInt(sttime.substring(0,2));
        int minutes = Integer.parseInt(sttime.substring(2, 4));
        int minTime = hour * 60 + minutes;

        String serviceKey = "?serviceKey=" + trainServiceKey;
        String stime = "&stime=" + sttime;

        // 필수옵션
        String essentialOpt = "&act=json";
        String essentialOpt1 = "&scode=" + scode;

        String url = trainServiceUrl + serviceKey + essentialOpt + essentialOpt1 + "&day=" + day + stime + "&enum=3&updown=0"  ;
        String url2 = trainServiceUrl + serviceKey + essentialOpt + essentialOpt1 + "&day=" + day + stime + "&enum=3&updown=1"  ;
//        System.out.println(url);

        List<TItemDTO> TrainJsonList = apiservice.getTrainJson(url);
        List<TItemDTO> TrainJsonList2 = apiservice.getTrainJson(url2);

        String downendcode = TrainJsonList.get(0).getEndcode();
        String upendcode = TrainJsonList2.get(0).getEndcode();

        // 종착역 이름 나오는거
        String DownendStationName =categoryservice.getStationName(downendcode);
        String UpendStationName =categoryservice.getStationName(upendcode);



        // 리스트로 만든값
        List<Integer> resultList1 = new ArrayList<>();  // 다대포해수욕장의 result 값들
        List<Integer> resultList2 = new ArrayList<>(); // 노포 result 값

//        System.out.println(DownendStationName);
        for (TItemDTO train : TrainJsonList) {
            int trainTimeHour = train.getHour();
            int trainTimeMinutes = train.getTime();
            int trainminTime = trainTimeHour * 60 + trainTimeMinutes;
            int result = trainminTime - minTime;

//            System.out.println("result = " + result);
            resultList1.add(result); // 다대포해수욕장의 result 값들

        }
//        System.out.println(UpendStationName);
        for(TItemDTO train : TrainJsonList2){
            int trainTimeHour = train.getHour();
            int trainTimeMinutes = train.getTime();
            int trainminTime = trainTimeHour * 60 + trainTimeMinutes;
            int result2 = trainminTime - minTime;

//            System.out.println("result2 = " + result2);
            resultList2.add(result2); // 노포의 result2 값들
        }
        // map List 로 만들어서 값 넘기기.
        Map<String, List<Integer>> resultMap = new HashMap<>();
        resultMap.put(DownendStationName, resultList1);
        resultMap.put(UpendStationName, resultList2);


        return resultMap;
    }

    @GetMapping("/app/station/{scode}")
    public List<StationInfoDTO> getStationInfo(@PathVariable String scode) throws Exception {
        return stationservice.getStationInfoList(scode);
    }


    @GetMapping("/app/StationSheet/{scode}")
    public StationSheetDTO getStationSheet(@PathVariable String scode) throws Exception {
        // service에서 필요한 데이터 가져오기
        List<TimeUPDTO> timeups = stationservice.getTimeUpList(scode);
        List<TimeDownDTO> timedowns = stationservice.getTimeDownList(scode);

        // StationSheetDTO 생성 후 리스트 세팅
        StationSheetDTO stationSheet = new StationSheetDTO();
        stationSheet.setTimeups(timeups);
        stationSheet.setTimedowns(timedowns);

        // 결과 반환
        return stationSheet;
    }

}
