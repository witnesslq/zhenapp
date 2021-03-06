package com.zhenapp.mapper;

import com.zhenapp.po.TUserInfo;
import com.zhenapp.po.TUserInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TUserInfoMapper {
    int countByExample(TUserInfoExample example);

    int deleteByExample(TUserInfoExample example);

    int deleteByPrimaryKey(Integer userid);

    int insert(TUserInfo record);

    int insertSelective(TUserInfo record);

    List<TUserInfo> selectByExample(TUserInfoExample example);

    TUserInfo selectByPrimaryKey(Integer userid);

    int updateByExampleSelective(@Param("record") TUserInfo record, @Param("example") TUserInfoExample example);

    int updateByExample(@Param("record") TUserInfo record, @Param("example") TUserInfoExample example);

    int updateByPrimaryKeySelective(TUserInfo record);

    int updateByPrimaryKey(TUserInfo record);
}