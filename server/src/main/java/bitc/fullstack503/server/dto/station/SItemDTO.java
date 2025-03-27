package bitc.fullstack503.server.dto.station;


import lombok.Data;

@Data
public class SItemDTO {
    private String dist;
    private String endSc;
    private String endSn;
    private String exchange;
    private String startSc;
    private String startSn;
    private String stoppingTime;
    private String time;
}
