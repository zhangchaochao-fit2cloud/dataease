package io.dataease.api.map;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.dataease.api.map.vo.AreaNode;
import io.dataease.api.map.vo.CustomGeoArea;
import io.dataease.api.map.vo.CustomGeoSubArea;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "系统设置:自定义地理区域")
@ApiSupport(order = 799)
public interface CustomGeoApi {

    @Operation(summary = "查询自定义地理区域")
    @GetMapping("/geoArea/list")
    List<CustomGeoArea> listCustomGeoArea();

    @Operation(summary = "查询自定义地理区域详情")
    @GetMapping("/geoArea/{id}")
    List<CustomGeoSubArea> getCustomGeoArea(@PathVariable("id") String id);

    @Operation(summary = "删除自定义地理区域")
    @DeleteMapping("/geoArea/{id}")
    void deleteCustomGeoArea(@PathVariable("id") String id);

    @Operation(summary = "保存自定义地理区域")
    @PostMapping("/geoArea/save")
    void saveCustomGeoArea(@RequestBody CustomGeoArea geoArea);

    @Operation(summary = "删除自定义地理子区域")
    @DeleteMapping("/geoSubArea/{id}")
    void deleteCustomGeoSubArea(@PathVariable("id") long id);

    @Operation(summary = "保存自定义地理子区域")
    @PostMapping("/geoSubArea/save")
    void saveCustomGeoSubArea(@RequestBody CustomGeoSubArea geoSubArea);

    @Operation(summary = "获取子区域下拉框可选列表")
    @GetMapping("/geoSubArea/options")
    List<AreaNode> getCustomGeoSubAreaOptions();
}
