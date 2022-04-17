package com.dj1801zdn.licence.mapper;

import com.dj1801zdn.licence.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from user where name = #{name} ")
    public User login(String name);

    @Insert("insert into user (name,password) " +
            "values (#{name},#{password})")
    public User signin(String name, String password);

    @Insert("insert into licence (licence,type) " +
            "values (#{licence},#{type})")
    public int generate(String licence,String type);

    @Update("update user set " +
            "licence = #{licence} ," +
            "type = #{type} " +
            "where userid = #{userid} ")
    public int userselect(User user);
}

