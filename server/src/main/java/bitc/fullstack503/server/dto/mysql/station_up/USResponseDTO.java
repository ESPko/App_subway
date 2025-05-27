package bitc.fullstack503.server.dto.mysql.station_up;

import lombok.Data;

@Data
public class USResponseDTO {
    private USHeaderDTO header;
    private USBodyDTO body;
}
