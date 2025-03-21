package bitc.fullstack503.server.appserver;

import bitc.fullstack503.server.dto.UserDTO;
import bitc.fullstack503.server.dto.station.SItemDTO;
import bitc.fullstack503.server.dto.train.TItemDTO;
import bitc.fullstack503.server.service.Apiservice;
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

  @Value("${team3.train.service.key}")
  private String trainServiceKey;

  @Value("${team3.station.service.url}")
  private String stationServiceUrl;

  @Value("${team3.train.service.url}")
  private String trainServiceUrl;

  @Autowired
  private Apiservice apiservice;


  @GetMapping("/gettest1")
  public String getTest1() {
    System.out.println("*** retrofit으로 gettest1에 접속 ***");

    return "get test1";
  }

  @GetMapping("/app")
  public String getApp() throws Exception {

    String serviceKey = "?serviceKey=" + stationServiceKey;

    // 필수옵션
    String essentialOpt = "&act=json";
    // 선택옵션 scode 는 역외부코드 입력하는부분
    String scode = "101";
    String Opt1 = "&scode=" + scode;

    String url = stationServiceUrl + serviceKey + essentialOpt + Opt1;

    List<SItemDTO> StationJsonList = apiservice.getStationJson(url);


    return StationJsonList.toString();
  }

  @GetMapping("/android")
  public String getAndroid() throws Exception {

    String serviceKey = "?serviceKey=" + trainServiceKey;

    // 필수옵션
    // 선택옵션 scode 는 역외부코드 입력하는부분
    String trainServiceKeyParam = "?serviceKey=" + trainServiceKey;
    String essentialOpt2 = "&act=json";
    String scode = "119";
    String Opt2 = "&scode=" + scode;
    String trainDay = "&day=1";  // 평일
    String trainUpDown = "&updown=1";  // 상행선
    String trainStartTime = "&stime=1227";  // 시작 시간 (예: 1300 = 13:00)
    String trainEndTime = "&etime=1400";  // 끝 시간 (예: 1400 = 14:00)
    String trainEnum = "&enum=5";  // 검색 시 리스트할 갯수
    String trainAct = "&act=json";  // 응답 형식 (json)



    String trainUrl = trainServiceUrl + trainServiceKeyParam + trainDay + trainUpDown + trainStartTime + trainEndTime + trainEnum + trainAct+ essentialOpt2 + Opt2;


    List<TItemDTO> TrainJsonList = apiservice.getTrainJson(trainUrl);

    // 데이터가 잘 조회되었는지 확인
    if (TrainJsonList == null || TrainJsonList.isEmpty()) {
      System.out.println("TrainJsonList가 비어 있습니다.");
      return "데이터 없음.";
    }

    // 데이터 출력 (JSON 형태로 보기 쉽게)
    return TrainJsonList.toString();
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












