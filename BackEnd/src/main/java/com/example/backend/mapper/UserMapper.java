package com.example.backend.mapper;

import com.example.backend.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username} and password = #{password};")
    User getUsernameAndPassword(User user);

    @Select("select * from user where username = #{username} or email = #{email};")
    List<User> findUserByEmailAndUsername();

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user (id, username, email, password, image, create_time, family_id) values (#{id},#{username},#{email},#{password},#{image},#{createTime},#{familyId});")
    void add(User user);

    @Select("select * from user where username = #{username};")
    User getByUsername(User user);
}
