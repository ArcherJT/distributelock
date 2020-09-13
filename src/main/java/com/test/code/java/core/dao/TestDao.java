package com.test.code.java.core.dao;

import com.test.code.java.core.domain.Test;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TestDao {

    @Select("SELECT t.id id,t.user_name userName ,t.user_id userId,t.role role,t.sex sex FROM t_financing_test t ")
    List<Test> queryTest();

    String INSERT_TEST = "INSERT INTO `t_financing_test` (`user_name`, `user_id`,`created_by`) \n" +
            "VALUES(#{userName},#{userId},#{createdBy})";
    @Insert(INSERT_TEST)
    int insertTest(@Param("userName") String userName, @Param("userId") String userId,
                   @Param("createdBy") String createdBy);

    String update_test = " UPDATE t_financing_test t SET t.created_by = #{createdBy} WHERE t.id = 414";
    @Update(update_test)
    int updateTest(@Param("createdBy") String createdBy);

    @Select(" SELECT t.created_by FROM  t_financing_test t WHERE t.id = 414")
    String queryTestById();
}
