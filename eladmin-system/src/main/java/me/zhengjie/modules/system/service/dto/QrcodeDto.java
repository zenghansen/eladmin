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
package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author zenghs
* @date 2024-03-02
**/
@Data
public class QrcodeDto implements Serializable {

    /** ID */
    private Long qrcodeId;

    /** 二维码名称 */
    private String name;

    /** 二维码标识 */
    private String code;

    /** 二维码状态 */
    private Boolean enabled;

    /** 排序 */
    private Integer qrcodeSort;

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updateBy;

    /** 创建日期 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 二维码路径 */
    private String imgPath;

    /** 访问次数 */
    private Integer visits;
}