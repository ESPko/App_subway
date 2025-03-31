package bitc.fullstack503.server.dto.mysql;

import lombok.Data;

import java.util.List;

@Data
public class StationSheetDTO {
    private List<TimeUPDTO> timeups;
    private List<TimeDownDTO> timedowns;
}
