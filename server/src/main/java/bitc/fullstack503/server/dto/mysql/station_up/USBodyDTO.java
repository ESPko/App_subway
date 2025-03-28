package bitc.fullstack503.server.dto.mysql.station_up;

import lombok.Data;

import java.util.List;

@Data
public class USBodyDTO {
    private List<USItemDTO> item;
    private String numOfRows; // int 로 해도됨
    private String pageNo;
    private String totalCount;
}
