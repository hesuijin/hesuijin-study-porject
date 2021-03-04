package com.example.jwt.secuirty.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.jwt.base.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 主键ID数据操作接口
 * @author hsj 2021-03-04
*/
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {
}