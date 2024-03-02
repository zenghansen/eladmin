/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import me.zhengjie.base.BaseEntity;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author zenghs
* @date 2024-03-02
**/
@Entity
@Data
@Table(name="sys_qrcode")
public class Qrcode extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`qrcode_id`")
    @ApiModelProperty(value = "ID")
    private Long qrcodeId;

    @Column(name = "`name`",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "二维码名称")
    private String name;

    @Column(name = "`code`",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "二维码标识")
    private String code;

    @Column(name = "`enabled`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "二维码状态")
    private Boolean enabled;

    @Column(name = "`qrcode_sort`")
    @ApiModelProperty(value = "排序")
    private Integer qrcodeSort;

    @Column(name = "`img_path`",nullable = false)
    @ApiModelProperty(value = "二维码路径")
    private String imgPath;

    @Column(name = "`visits`")
    @ApiModelProperty(value = "访问次数")
    private Integer visits;

    public void copy(Qrcode source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
