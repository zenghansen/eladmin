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
package me.zhengjie.modules.system.rest;

import cn.hutool.core.util.IdUtil;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.system.domain.Qrcode;
import me.zhengjie.modules.system.service.QrcodeService;
import me.zhengjie.modules.system.service.dto.QrcodeQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.modules.system.service.dto.QrcodeDto;

/**
* @website https://eladmin.vip
* @author zenghs
* @date 2024-03-02
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "qrcode管理")
@RequestMapping("/api/qrcode")
public class QrcodeController {

    private final QrcodeService qrcodeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('qrcode:list')")
    public void exportQrcode(HttpServletResponse response, QrcodeQueryCriteria criteria) throws IOException {
        qrcodeService.download(qrcodeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询qrcode")
    @ApiOperation("查询qrcode")
    @PreAuthorize("@el.check('qrcode:list')")
    public ResponseEntity<PageResult<QrcodeDto>> queryQrcode(QrcodeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(qrcodeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @AnonymousGetMapping("/{code}")
    @Log("查询qrcodebyid")
    @ApiOperation("查询qrcodebyCode")
    public ResponseEntity<QrcodeDto> queryQrcodeByCode(@PathVariable String code){
        return new ResponseEntity<>(qrcodeService.findByCode(code),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增qrcode")
    @ApiOperation("新增qrcode")
    @PreAuthorize("@el.check('qrcode:add')")
    public ResponseEntity<Object> createQrcode(@Validated @RequestBody Qrcode resources){
        resources.setCode(IdUtil.getSnowflakeNextIdStr());
        qrcodeService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改qrcode")
    @ApiOperation("修改qrcode")
    @PreAuthorize("@el.check('qrcode:edit')")
    public ResponseEntity<Object> updateQrcode(@Validated @RequestBody Qrcode resources){
        qrcodeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除qrcode")
    @ApiOperation("删除qrcode")
    @PreAuthorize("@el.check('qrcode:del')")
    public ResponseEntity<Object> deleteQrcode(@RequestBody Long[] ids) {
        qrcodeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}