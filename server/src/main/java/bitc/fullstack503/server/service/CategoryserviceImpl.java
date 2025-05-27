package bitc.fullstack503.server.service;

import bitc.fullstack503.server.dto.mysql.CategoryDTO;
import bitc.fullstack503.server.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryserviceImpl implements Categoryservice {


    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getCategoryList() throws Exception {
        return categoryMapper.getCategoryList();
    }

    @Override
    public List<CategoryDTO> getCategoryLineList(String scode) throws Exception {
        return categoryMapper.getCategoryLineList(scode);
    }

    @Override
    public String getStationName(String downendcode) throws Exception {
        return categoryMapper.getStationName(downendcode);
    }
}
