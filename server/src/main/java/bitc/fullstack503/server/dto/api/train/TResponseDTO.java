package bitc.fullstack503.server.dto.api.station.train;


import lombok.Data;

@Data
public class TResponseDTO {
    private THeaderDTO header;
    private TBodyDTO body;
}
