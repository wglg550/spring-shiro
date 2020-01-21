package com.spring.shiro.demo.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @Description: 基础entity
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/12/6
 */
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)//(AuditingEntityListener.class):声明实体监听器,用于实体修改时做处理
@MappedSuperclass//声明该类为实体父类,不会映射单独的表,而是把字段映射到子类表中
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @CreatedBy
//    @Column(name = "create_by")
//    @ApiModelProperty(value = "创建人id")
//    private String createBy;

//    @CreatedBy
//    @Column(name = "create_name")
//    @ApiModelProperty(value = "创建人名称")
//    private String createName;

//    @CreatedDate
//    @Column(name = "create_time")
//    @ApiModelProperty(value = "创建时间")
//    private Date createTime;
//
//    @LastModifiedBy
//    @Column(name = "last_update_by")
//    @ApiModelProperty(value = "修改人id")
//    private String lastUpdateBy;

//    @LastModifiedBy
//    @Column(name = "last_update_name")
//    @ApiModelProperty(value = "修改人名称")
//    private String lastUpdateName;

//    @LastModifiedDate
//    @Column(name = "last_update_time")
//    @ApiModelProperty(value = "修改时间")
//    private Date lastUpdateTime;
}
