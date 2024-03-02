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
package me.zhengjie.modules.system.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.system.domain.Qrcode;
import me.zhengjie.modules.system.service.dto.QrcodeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @website https://eladmin.vip
* @author zenghs
* @date 2024-03-02
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QrcodeMapper extends BaseMapper<QrcodeDto, Qrcode> {

}