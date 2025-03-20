package bitc.fullstack503.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

//  안드로이드 앱과 데이터를 주고 받기 위한 DTO 클래스
@Data
public class UserDTO {

//  안드로이드 앱에서 전달한 DTO 타입 객체에 데이터가 null 이 나올 경우 이름이 일치하지 않을 수 있음
//  @JsonProperty 를 사용하여 앱에서 전달하는 이름과 일치시켜 줌
  @JsonProperty("userId")
  private String userId;

  @JsonProperty("userPw")
  private String userPw;

  @JsonProperty("userName")
  private String userName;

  @JsonProperty("userEmail")
  private String userEmail;

}












