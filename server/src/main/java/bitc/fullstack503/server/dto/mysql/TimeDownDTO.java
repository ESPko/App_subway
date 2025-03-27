package bitc.fullstack503.server.dto.mysql;

import lombok.Data;

@Data
public class TimeDownDTO {
    private int downIdx;
    private int scode;
    private String day;
    private String sat;
    private String holi;

}
