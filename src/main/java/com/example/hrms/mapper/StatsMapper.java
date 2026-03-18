package com.example.hrms.mapper;

import com.example.hrms.entity.Stats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 统计Mapper（仅返回主实体，避免跨包访问子实体）
 */
@Mapper
public interface StatsMapper {

}