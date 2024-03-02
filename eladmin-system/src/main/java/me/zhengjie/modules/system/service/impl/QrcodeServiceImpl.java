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

import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.modules.system.domain.Qrcode;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.QrcodeRepository;
import me.zhengjie.modules.system.service.QrcodeService;
import me.zhengjie.modules.system.service.dto.QrcodeDto;
import me.zhengjie.modules.system.service.dto.QrcodeQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.QrcodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author zenghs
* @date 2024-03-02
**/
@Service
@RequiredArgsConstructor
public class QrcodeServiceImpl implements QrcodeService {

    private final QrcodeRepository qrcodeRepository;
    private final QrcodeMapper qrcodeMapper;

    @Override
    public PageResult<QrcodeDto> queryAll(QrcodeQueryCriteria criteria, Pageable pageable){
        Page<Qrcode> page = qrcodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(qrcodeMapper::toDto));
    }

    @Override
    public List<QrcodeDto> queryAll(QrcodeQueryCriteria criteria){
        return qrcodeMapper.toDto(qrcodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public QrcodeDto findById(Long qrcodeId) {
        Qrcode qrcode = qrcodeRepository.findById(qrcodeId).orElseGet(Qrcode::new);
        ValidationUtil.isNull(qrcode.getQrcodeId(),"Qrcode","qrcodeId",qrcodeId);
        return qrcodeMapper.toDto(qrcode);
    }

    @Override
    public QrcodeDto findByCode(String code) {
        Qrcode qrcode = qrcodeRepository.findByCode(code);
        if (qrcode == null) {
            throw new EntityNotFoundException(Qrcode.class, "code", code);
        } else {
            return qrcodeMapper.toDto(qrcode);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Qrcode resources) {
        if(qrcodeRepository.findByName(resources.getName()) != null){
            throw new EntityExistException(Qrcode.class,"name",resources.getName());
        }
        qrcodeRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Qrcode resources) {
        Qrcode qrcode = qrcodeRepository.findById(resources.getQrcodeId()).orElseGet(Qrcode::new);
        ValidationUtil.isNull( qrcode.getQrcodeId(),"Qrcode","id",resources.getQrcodeId());
        Qrcode qrcode1 = null;
        qrcode1 = qrcodeRepository.findByName(resources.getName());
        if(qrcode1 != null && !qrcode1.getQrcodeId().equals(qrcode.getQrcodeId())){
            throw new EntityExistException(Qrcode.class,"name",resources.getName());
        }
        qrcode.copy(resources);
        qrcodeRepository.save(qrcode);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long qrcodeId : ids) {
            qrcodeRepository.deleteById(qrcodeId);
        }
    }

    @Override
    public void download(List<QrcodeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QrcodeDto qrcode : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("二维码名称", qrcode.getName());
            map.put("二维码状态", qrcode.getEnabled());
            map.put("排序", qrcode.getQrcodeSort());
            map.put("创建者", qrcode.getCreateBy());
            map.put("更新者", qrcode.getUpdateBy());
            map.put("创建日期", qrcode.getCreateTime());
            map.put("更新时间", qrcode.getUpdateTime());
            map.put("二维码路径", qrcode.getImgPath());
            map.put("访问次数", qrcode.getVisits());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}