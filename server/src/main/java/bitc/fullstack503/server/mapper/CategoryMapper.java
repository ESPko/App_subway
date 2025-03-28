package bitc.fullstack503.server.mapper;

import bitc.fullstack503.server.dto.mysql.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryDTO> getCategoryList() throws Exception;

    List<CategoryDTO> getCategoryLineList(String scode) throws Exception;

    String getStationName(String endcode) throws Exception;
}
