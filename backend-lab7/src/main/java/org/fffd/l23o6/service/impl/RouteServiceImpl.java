package org.fffd.l23o6.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.mapper.RouteMapper;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.fffd.l23o6.service.RouteService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteDao routeDao;

    /**
     * 添加路线
     *
     * @param name       路线名称
     * @param stationIds 站点ID列表
     */
    @Override
    public void addRoute(String name, List<Long> stationIds) {
        RouteEntity route = RouteEntity.builder().name(name).stationIds(stationIds).build();
        routeDao.save(route);
    }

    /**
     * 获取路线列表
     *
     * @return 路线VO列表
     */
    @Override
    public List<RouteVO> listRoutes() {
        return routeDao.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream()
                .map(RouteMapper.INSTANCE::toRouteVO)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取路线详情
     *
     * @param id 路线ID
     * @return 路线VO
     */
    @Override
    public RouteVO getRoute(Long id) {
        RouteEntity entity = routeDao.findById(id).get();
        return RouteMapper.INSTANCE.toRouteVO(entity);
    }

    /**
     * 编辑路线
     *
     * @param id         路线ID
     * @param name       路线名称
     * @param stationIds 站点ID列表
     */
    @Override
    public void editRoute(Long id, String name, List<Long> stationIds) {
        routeDao.save(routeDao.findById(id).get().setStationIds(stationIds).setName(name));
    }

    /**
     * 删除路线
     *
     * @param routeId 路线ID
     */
    @Override
    public void delRoute(Long routeId) {
        routeDao.deleteById(routeId);
    }
}
