package bitc.fullstack503.server.dto.mysql.station_down;


import lombok.Data;

@Data
public class DSItemDTO {
    private String startSn;
    private String startSc;
    private String endSn;
    private String endSc;
    private Integer dist;
    private String time;
    private String stoppingTime;
    private String exchange;


}


