package com.example.security.mvc.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.security.base.dto.JwtUser;
import com.example.security.base.entity.User;
import com.example.security.base.request.LoginRequest;
import com.example.security.component.UserComponent;
import com.example.security.mvc.dao.UserMapper;
import com.example.security.mvc.service.AuthService;
import com.example.security.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author hesuijin
 * @Description
 * @Param
 * @Return
 * @Date 2021/3/4
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final UserComponent userComponent;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public String createToken(LoginRequest loginRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",loginRequest.getUsername());
        queryWrapper.last("limit 1");
        User user = userMapper.selectOne(queryWrapper);
        if (!userComponent.userCheck(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("The user name or password is not correct.");
        }
        //生成jwtUser对象
        JwtUser jwtUser = new JwtUser(user);
        if (!jwtUser.isEnabled()) {
            throw new BadCredentialsException("User is forbidden to login");
        }
        List<String> authorities = jwtUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        //生成token
        String token = JwtTokenUtils.createToken(user.getUserName(), user.getId().toString(), authorities, loginRequest.getRememberMe());
        stringRedisTemplate.opsForValue().set(user.getId().toString(), token);
        return token;
    }

    @Override
    public void removeToken() {
        stringRedisTemplate.delete(userComponent.getCurrentUser().getId().toString());
    }
}
