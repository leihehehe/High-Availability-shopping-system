package com.leih.shopping.db.mappers;

import com.leih.shopping.db.po.Deal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface DealMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Deal record);

    int insertSelective(Deal record);

    Deal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Deal record);

    int updateByPrimaryKey(Deal record);

    List<Deal> queryDealsByStatus(int activityStatus);
    int lockStock(Long id);
    int deductStock(Long id);
}