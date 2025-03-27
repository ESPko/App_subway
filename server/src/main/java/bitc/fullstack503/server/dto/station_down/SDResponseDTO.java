package bitc.fullstack503.server.dto.station_down;

import lombok.Data;

@Data
public class SDResponseDTO {
    private SDHeaderDTO header;
    private SDBodyDTO body;
}
