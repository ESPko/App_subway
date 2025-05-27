package bitc.fullstack503.server.appserver;

import bitc.fullstack503.server.dto.UserDTO;
import bitc.fullstack503.server.dto.api.train.TItemDTO;
import bitc.fullstack503.server.dto.mysql.CategoryDTO;
import bitc.fullstack503.server.service.Apiservice;
import bitc.fullstack503.server.service.Categoryservice;
import bitc.fullstack503.server.service.StationService;
import bitc.fullstack503.server.service.Testservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//  서버 기본 주소
@RequestMapping("/app")
@RestController
public class AppServerController {

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
  private Testservice testservice;

  @Autowired
  private StationService stationservice;

  @GetMapping("/gettest1")
  public String getTest1() {
    System.out.println("*** retrofit으로 gettest1에 접속 ***");

    return "get test1";
  }

  // 지하철 호선 정보
  @GetMapping("/app/category")
  public List<CategoryDTO> getCategory() throws Exception {

    List<CategoryDTO> categoryList = categoryservice.getCategoryList();

    return categoryList;
  }

  // 지하철 호선 이름
  @GetMapping("/app/category/{line}")
  public List<CategoryDTO> getCategory(@PathVariable String line) throws Exception {
      line = "1";

      List<CategoryDTO> categoryList = categoryservice.getCategoryLineList(line);

      return categoryList;
  }


 // 지하철 역간 이동시간
  @GetMapping("/time/total/{stStation}/{edStation}")
  public int getDistance(@PathVariable String stStation, @PathVariable String edStation) throws Exception {

    int result;
    // startSc와 endSc에 해당하는 dist 값을 합산하여 반환


    int stStationInt = Integer.parseInt(stStation);
    int edStationInt = Integer.parseInt(edStation);


    if (stStationInt < edStationInt) {


      result = stationservice.getTimeTotalUp(stStationInt, edStationInt);

      System.out.println("result : " + result);

    }
    else {

      result = stationservice.getTimeTotalDown(stStationInt, edStationInt);
    }

    return result;
  }

  // 지하철 경유 갯수
  @GetMapping("/station/exchange/{stStation}/{edStation}")
  public int getExchange(@PathVariable String stStation, @PathVariable String edStation) throws Exception {

    int result;
    // startSc와 endSc에 해당하는 dist 값을 합산하여 반환


    int stStationInt = Integer.parseInt(stStation);
    int edStationInt = Integer.parseInt(edStation);


    if (stStationInt < edStationInt) {

      result = stationservice.getExchangeUp(stStationInt, edStationInt);
    }

    else {

      result = stationservice.getExchangeDown(stStationInt, edStationInt);
    }

    return result;
  }
//   현재 시간과 가까운 지하철 시간 선택( 평일 )

  @GetMapping("/app/trainTime/{stScode}/{edScode}/{sttime}/{day}")
  public String getTrain(@PathVariable String stScode, @PathVariable String edScode, @PathVariable String sttime, @PathVariable String day) throws Exception {
//    int hour = Integer.parseInt(sttime.substring(0, 2));
//    int minutes = Integer.parseInt(sttime.substring(2, 4));
//    int minTime = hour * 60 + minutes;

    System.out.println("sttime : " + sttime);

    int startScode = Integer.parseInt(stScode);
    int endScode = Integer.parseInt(edScode);

    String serviceKey = "?serviceKey=" + trainServiceKey;
    String stime = "&stime=" + sttime;

    // 필수옵션
    String essentialOpt = "&act=json";
    String essentialOpt1 = "&scode=" + stScode;
    String url;
    if(startScode - endScode >= 0){ // 상행
      url = trainServiceUrl + serviceKey + essentialOpt + essentialOpt1 + "&day=" + day + stime + "&enum=1&updown=1";
    }
    else { // 하행
      url = trainServiceUrl + serviceKey + essentialOpt + essentialOpt1 + "&day=" + day + stime + "&enum=1&updown=0";
    }

    List<TItemDTO> TrainJsonList = apiservice.getTrainJson(url);


    int TrainHour = TrainJsonList.get(0).getHour(); // 15 12 09 11 12
    int TrainMinute = TrainJsonList.get(0).getTime(); // 23 30 40 50 60



    String hourString = String.valueOf(TrainHour);

    String formattedTime = String.format("%02d", TrainMinute);

    String trainTime = hourString + ":" + formattedTime;

    // 12 : 53 1253 -> 코틀린에서 12:53

  return trainTime;


  }













//  파라미터로만 데이터 받을 경우
  @GetMapping("/gettest2")
  public String getTest2(@RequestParam("param1") String param1, @RequestParam("param2") String param2) {
    System.out.println("*** retrofit으로 gettest2에 접속 ***");
    System.out.println("param1: " + param1);
    System.out.println("param2: " + param2);

    return "get test2";
  }

//  REST API 방식으로 파라미터를 받을 경우
  @GetMapping("/gettest3/{param1}/{param2}")
  public String getTest3(@PathVariable("param1") String param1, @PathVariable("param2") String param2) {
    System.out.println("*** retrofit으로 gettest3에 접속 ***");
    System.out.println("param1: " + param1);
    System.out.println("param2: " + param2);

    return "get test3";
  }



  @GetMapping("/app/{param1}/{param2}")
  public int getApi2(@PathVariable("param1") String param1, @PathVariable("param2") String param2) {
    System.out.println("안드로이드 스튜디오에서 수정된 값을 전달받음");
    System.out.println("수정된 dist: " + param1);
    System.out.println("수정된 endSc: " + param2);

    int result =0;

    int num0 = Integer.parseInt(param1);
    int num1 = Integer.parseInt(param2);

    result = num0 + num1;



    return result;
  }


//  @GetMapping("/app/{param1}")
//  public List<USItemDTO> getApi3(@PathVariable("param1") String param1) throws Exception {
//    String serviceKey = "?serviceKey=" + stationServiceKey;
//
//    // 필수옵션
//    String essentialOpt = "&act=json";
//    // 선택옵션 scode 는 역외부코드 입력하는부분
//    String scode = param1;
//
//    String Opt1 = "&scode=" + scode;
//
//    String url = stationServiceUrl + serviceKey + essentialOpt + Opt1;
//
//    List<USItemDTO> StationJsonList = apiservice.getStationJson(url);
//
//    if(StationJsonList != null){
//      return StationJsonList;
//    }else{
//      return null;
//    }
//
//  }

  @PostMapping("/posttest1")
  public String postTest1() {
    System.out.println("*** retrofit으로 posttest1에 접속 ***");

    return "post test1";
  }

//  DTO 타입을 파라미터로 받을 경우
  @PostMapping("/posttest2")
  public String postTest2(@RequestBody UserDTO user) {
    System.out.println("*** retrofit으로 posttest2에 접속 ***");

    System.out.println("userId : " + user.getUserId());
    System.out.println("userPwd : " + user.getUserPw());
    System.out.println("userNickName : " + user.getUserName());
    System.out.println("userEmail : " + user.getUserEmail());

    return "post test2";
  }

  @PutMapping("/puttest1")
  public String putTest1() {
    System.out.println("*** retrofit으로 puttest1에 접속 ***");

    return "put test1";
  }

//  DTO 와 일반 데이터를 파라미터로 받을 경우
  @PutMapping("/puttest2")
  public String putTest2(@RequestBody UserDTO user, @RequestParam("param1") String param1) {
    System.out.println("*** retrofit으로 puttest2 접속 ***");
    System.out.println("userId : " + user.getUserId());
    System.out.println("userPwd : " + user.getUserPw());
    System.out.println("userNickName : " + user.getUserName());
    System.out.println("userEmail : " + user.getUserEmail());

    System.out.println("param1: " + param1);

    return "put test2";
  }

  @DeleteMapping("/deletetest1")
  public String deletetest1(@RequestParam("param1") String param1) {
    System.out.println("*** retrofit으로 deletetest1에 접속 ***");
    System.out.println("param1: " + param1);

    return "delete test1";
  }
}












