package bitc.fullstack503.server.dto.station_down;

import lombok.Data;

import java.util.List;

@Data
public class SDBodyDTO {
    private List<SDItemDTO> item;
    private String numOfRows; // int 로 해도됨
    private String pageNo;
    private String totalCount;
}
