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
package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Qrcode;
import me.zhengjie.modules.system.service.dto.QrcodeDto;
import me.zhengjie.modules.system.service.dto.QrcodeQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
public interface QrcodeService {

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    QrcodeDto findById(Long id);

    /**
     * 创建
     * @param resources /
     * @return /
     */
    void create(Qrcode resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Qrcode resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult<QrcodeDto> queryAll(QrcodeQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部数据
     * @param criteria /
     * @return /
     */
    List<QrcodeDto> queryAll(QrcodeQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<QrcodeDto> queryAll, HttpServletResponse response) throws IOException;

}
