package com.example.security.base.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.security.base.BaseModel;
import com.example.security.component.UserComponent;
import com.example.security.config.ApplicationContextHelper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 使用充血模型
 * 主键Id：user
 * @author hsj 2021-03-04
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Controller
public class User extends BaseModel {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String userName;
    private String fullName;
    private String password;
    private Boolean enabled;


//    @TableField(exist = false)
//    否则使用mybatisPlus的时候 会把该属性 作为sql  select 的字段植而报错
//    @Autowired
//    new出来的对象 无法直接引用spring容器中的对象
//    private UserRoleMapper userRoleMapper;


    //使用JSON 转换打印时过滤掉该get方法
    @JSONField(serialize = false)
    public List<SimpleGrantedAuthority> getRoles()  {
        Method mehtod;
        Class componentClass = UserComponent.class;
//        反射出来的类 使用构造方法创建的对象也无法引用
//        Object obj = componentClass.getConstructor().newInstance();
        Object obj = ApplicationContextHelper.popBean(componentClass);
        Object objInvoke = new Object();
        try {
            mehtod = componentClass.getMethod("getRoleNameByUserName", String.class);
             objInvoke = mehtod.invoke(obj, this.userName);
        }catch (Exception e){
            log.info("User实体类反射异常： "+e.getMessage(),e);
        }
        return  (List<SimpleGrantedAuthority>) objInvoke;
    }

}