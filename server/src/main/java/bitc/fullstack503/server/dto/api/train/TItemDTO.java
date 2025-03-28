package bitc.fullstack503.server.dto.api.train;


import lombok.Data;

@Data
public class TItemDTO {
    private String day;
    private String endcode;
    private String engname;
    private Integer hour;
    private String line;
    private String scode;
    private String sname;
    private Integer time;
    private String trainno;
    private String updown;

}
