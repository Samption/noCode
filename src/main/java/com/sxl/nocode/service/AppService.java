package com.sxl.nocode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.sxl.nocode.model.dto.app.AppQueryRequest;
import com.sxl.nocode.model.entity.App;
import com.sxl.nocode.model.vo.AppVO;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/Samption">Samption</a>
 */
public interface AppService extends IService<App> {
    /**
     * 获取应用封装类
     * @param app
     * @return
     */
    AppVO getAppVO(App app);

    /**
     * 获取应用封装类列表
     * @param appList
     * @return
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 构造应用查询条件
     * @param appQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);
}
