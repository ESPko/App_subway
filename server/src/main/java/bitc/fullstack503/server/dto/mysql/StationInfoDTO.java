package bitc.fullstack503.server.dto.mysql;


import lombok.Data;

@Data
public class StationInfoDTO {
    private int scode;
    private String door;
    private String toilet;
    private String across;
    private String flatform;
    private String storage;
    private String util;
    private String address;
    private String number;
}
