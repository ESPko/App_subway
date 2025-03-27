package bitc.fullstack503.server.dto.train;

import bitc.fullstack503.server.dto.station.SItemDTO;
import lombok.Data;

import java.util.List;

@Data
public class TBodyDTO {
    private List<TItemDTO> item;
    private String numOfRows;
    private String pageNo;
    private String totalCount;

}
