package io.dataease.share.server;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dataease.api.xpack.share.ShareTicketApi;
import io.dataease.api.xpack.share.request.TicketCreator;
import io.dataease.api.xpack.share.request.TicketDelRequest;
import io.dataease.api.xpack.share.request.TicketSwitchRequest;
import io.dataease.api.xpack.share.vo.TicketVO;
import io.dataease.commons.utils.CodingUtil;
import io.dataease.share.manage.ShareTicketManage;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class ShareTicketServer implements ShareTicketApi {

    @Resource
    private ShareTicketManage shareTicketManage;

    @Override
    public String saveTicket(TicketCreator creator) {
        return shareTicketManage.saveTicket(creator);
    }

    @Override
    public void deleteTicket(TicketDelRequest request) {
        shareTicketManage.deleteTicket(request);
    }

    @Override
    public void switchRequire(TicketSwitchRequest request) {
        shareTicketManage.switchRequire(request);
    }

    @Override
    public IPage<TicketVO> pager(Long resourceId, int goPage, int pageSize) {
        Page<TicketVO> page = new Page<>(goPage, pageSize);
        return shareTicketManage.query(resourceId, page);
    }

    @Override
    public String tempTicket() {
        return CodingUtil.shortUuid();
    }

    @Override
    public Integer limit() {
        return shareTicketManage.getLimit();
    }
}
