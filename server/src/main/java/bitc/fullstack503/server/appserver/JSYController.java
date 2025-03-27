package bitc.fullstack503.server.appserver;

import bitc.fullstack503.server.dto.UserDTO;
import bitc.fullstack503.server.dto.mysql.CategoryDTO;
import bitc.fullstack503.server.dto.station.SItemDTO;
import bitc.fullstack503.server.dto.train.TItemDTO;
import bitc.fullstack503.server.service.Apiservice;
import bitc.fullstack503.server.service.Categoryservice;
import bitc.fullstack503.server.service.Testservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // select * from category where line = ? and scode = ?
    @GetMapping("/app/category/{scode}")
    public List<CategoryDTO> getStationListName(@PathVariable String scode) throws Exception {

        List<CategoryDTO> categoryList = categoryservice.getCategoryLineList(scode);

        return categoryList;
    }


    @GetMapping("/app/train/{scode}/{sttime}/{day}")
    public List<TItemDTO> gettrain(@PathVariable String scode,@PathVariable String sttime,@PathVariable String day) throws Exception {

        String serviceKey = "?serviceKey=" + trainServiceKey;

        int hours = Integer.parseInt(sttime.substring(0, 2));  // 앞 2자리 -> 시
        int minutes = Integer.parseInt(sttime.substring(2, 4));
        minutes += 20;
        if (minutes >= 60) {
            minutes -= 60;
            hours += 1;
        }

        // 4. 결과가 24시를 넘을 수 있으므로, 24시를 넘으면 0시로 설정
        if (hours >= 24) {
            hours -= 24;
        }

        // 5. 결과를 "HHMM" 형식으로 출력
        String edtime = String.format("%02d%02d", hours, minutes);


//    String updown = "&updown=1";
        String stime = "&stime=" + sttime;
        String etime = "&etime=" + edtime;

        // 필수옵션
        String essentialOpt = "&act=json";
        String essentialOpt1 = "&scode=" + scode;

        String url = trainServiceUrl + serviceKey + essentialOpt + essentialOpt1 + "&day=" + day + stime + etime  ;

        System.out.println(url);

        List<TItemDTO> TrainJsonList = apiservice.getTrainJson(url);

        return TrainJsonList;
    }


}
