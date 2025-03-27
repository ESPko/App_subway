package bitc.fullstack503.server.dto.train;


import lombok.Data;

@Data
public class TResponseDTO {
    private THeaderDTO header;
    private TBodyDTO body;
}
