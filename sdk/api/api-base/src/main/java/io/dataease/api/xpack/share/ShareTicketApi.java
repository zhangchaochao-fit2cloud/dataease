package io.dataease.api.xpack.share;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dataease.api.xpack.share.request.TicketCreator;
import io.dataease.api.xpack.share.request.TicketDelRequest;
import io.dataease.api.xpack.share.request.TicketSwitchRequest;
import io.dataease.api.xpack.share.vo.TicketVO;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "可视化管理:分享:TICKET")
public interface ShareTicketApi {

    @PostMapping("/saveTicket")
    @Operation(summary = "保存Ticket")
    String saveTicket(@RequestBody TicketCreator creator);

    @PostMapping("/delTicket")
    @Operation(summary = "删除Ticket")
    void deleteTicket(@RequestBody TicketDelRequest request);

    @PostMapping("/enableTicket")
    @Operation(summary = "切换Ticket必填状态")
    void switchRequire(@RequestBody TicketSwitchRequest request);

    @PostMapping("/pager/{resourceId}/{goPage}/{pageSize}")
    @Operation(summary = "根据资源查询Ticket")
    @Parameter(name = "resourceId", description = "资源ID", required = true, in = ParameterIn.PATH)
    IPage<TicketVO> pager(@PathVariable("resourceId") Long resourceId, @PathVariable("goPage") int goPage, @PathVariable("pageSize") int pageSize);

    @GetMapping("/tempTicket")
    @Operation(summary = "生成临时Ticket")
    String tempTicket();

    @GetMapping("/limit")
    @Hidden
    Integer limit();
}
