package bitc.fullstack503.server.dto.train;

import bitc.fullstack503.server.dto.station.SHeaderDTO;
import lombok.Data;

@Data
public class TResponseDTO {
    private THeaderDTO header;
    private TBodyDTO body;

}
