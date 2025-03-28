package bitc.fullstack503.server.dto.mysql.station_down;

import lombok.Data;

import java.util.List;

@Data
public class DSBodyDTO {
    private List<DSItemDTO> item;
    private String numOfRows; // int 로 해도됨
    private String pageNo;
    private String totalCount;
}
