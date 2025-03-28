package bitc.fullstack503.server.dto.api.station;

import lombok.Data;

import java.util.List;

@Data
public class SBodyDTO {
    private List<SItemDTO> item;
    private String numOfRows; // int 로 해도됨
    private String pageNo;
    private String totalCount;
}
