package com.sxl.nocode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.sxl.nocode.model.dto.user.UserQueryRequest;
import com.sxl.nocode.model.entity.User;
import com.sxl.nocode.model.vo.LoginUserVO;
import com.sxl.nocode.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author <a href="https://github.com/Samption">Samption</a>
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    public String getEncryptPassword(String userPassword);

    /**
     * 获取当前登录用户，service层不脱敏，因为可能被其他接口调用，等controller层调用时再脱敏
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);


    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息列表
     *
     * @param userList
     * @return
     */
    public List<UserVO> getUserVOList(List<User> userList);

     /**
      * 获取查询包装类
      *
      * @param userQueryRequest
      * @return
      */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);

}
