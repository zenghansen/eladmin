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
package me.zhengjie.modules.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.system.domain.Qrcode;
import me.zhengjie.modules.system.repository.QrcodeRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.QrcodeService;
import me.zhengjie.modules.system.service.dto.QrcodeDto;
import me.zhengjie.modules.system.service.dto.QrcodeQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.QrcodeMapper;
import me.zhengjie.utils.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "qrcode")
public class QrcodeServiceImpl implements QrcodeService {

    private final QrcodeRepository qrcodeRepository;
    private final QrcodeMapper qrcodeMapper;
    private final RedisUtils redisUtils;
    private final UserRepository userRepository;

    @Override
    public PageResult<QrcodeDto> queryAll(QrcodeQueryCriteria criteria, Pageable pageable) {
        Page<Qrcode> page = qrcodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(qrcodeMapper::toDto).getContent(),page.getTotalElements());
    }

    @Override
    public List<QrcodeDto> queryAll(QrcodeQueryCriteria criteria) {
        List<Qrcode> list = qrcodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return qrcodeMapper.toDto(list);
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public QrcodeDto findById(Long id) {
        Qrcode qrcode = qrcodeRepository.findById(id).orElseGet(Qrcode::new);
        ValidationUtil.isNull(qrcode.getId(),"Qrcode","id",id);
        return qrcodeMapper.toDto(qrcode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Qrcode resources) {
        Qrcode qrcode = qrcodeRepository.findByName(resources.getName());
        if(qrcode != null){
            throw new EntityExistException(Qrcode.class,"name",resources.getName());
        }
        qrcodeRepository.save(resources);
    }

    @Override
    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(Qrcode resources) {
        Qrcode qrcode = qrcodeRepository.findById(resources.getId()).orElseGet(Qrcode::new);
        Qrcode old = qrcodeRepository.findByName(resources.getName());
        if(old != null && !old.getId().equals(resources.getId())){
            throw new EntityExistException(Qrcode.class,"name",resources.getName());
        }
        ValidationUtil.isNull( qrcode.getId(),"Qrcode","id",resources.getId());
        resources.setId(qrcode.getId());
        qrcodeRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        qrcodeRepository.deleteAllByIdIn(ids);
        // 删除缓存
        redisUtils.delByKeys(CacheKey.JOB_ID, ids);
    }

    @Override
    public void download(List<QrcodeDto> qrcodeDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QrcodeDto qrcodeDTO : qrcodeDtos) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("岗位名称", qrcodeDTO.getName());
            map.put("岗位状态", qrcodeDTO.getEnabled() ? "启用" : "停用");
            map.put("创建日期", qrcodeDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
