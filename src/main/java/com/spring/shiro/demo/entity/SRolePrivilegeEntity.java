package com.spring.shiro.demo.entity;

import com.spring.shiro.demo.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "s_role_privilege", schema = "spring_cloud_demo", catalog = "")
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class SRolePrivilegeEntity extends BaseEntity {
    private Long roleId;
    private Long privilegeId;
}
