package bitc.fullstack503.server.dto.station_down;

import lombok.Data;

@Data
public class DSResponseDTO {
    private DSHeaderDTO header;
    private DSBodyDTO body;
}
