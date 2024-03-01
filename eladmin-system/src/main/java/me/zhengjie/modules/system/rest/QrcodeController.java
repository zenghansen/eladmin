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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Qrcode;
import me.zhengjie.modules.system.service.QrcodeService;
import me.zhengjie.modules.system.service.dto.QrcodeDto;
import me.zhengjie.modules.system.service.dto.QrcodeQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：二维码管理")
@RequestMapping("/api/qrcode")
public class QrcodeController {

    private final QrcodeService qrcodeService;
    private static final String ENTITY_NAME = "qrcode";

    @ApiOperation("导出二维码数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('qrcode:list')")
    public void exportQrcode(HttpServletResponse response, QrcodeQueryCriteria criteria) throws IOException {
        qrcodeService.download(qrcodeService.queryAll(criteria), response);
    }

    @ApiOperation("查询岗位")
    @GetMapping
    @PreAuthorize("@el.check('qrcode:list','user:list')")
    public ResponseEntity<PageResult<QrcodeDto>> queryQrcode(QrcodeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(qrcodeService.queryAll(criteria, pageable),HttpStatus.OK);
    }

    @Log("新增岗位")
    @ApiOperation("新增岗位")
    @PostMapping
    @PreAuthorize("@el.check('qrcode:add')")
    public ResponseEntity<Object> createQrcode(@Validated @RequestBody Qrcode resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        qrcodeService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改岗位")
    @ApiOperation("修改岗位")
    @PutMapping
    @PreAuthorize("@el.check('qrcode:edit')")
    public ResponseEntity<Object> updateQrcode(@Validated(Qrcode.Update.class) @RequestBody Qrcode resources){
        qrcodeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除岗位")
    @ApiOperation("删除岗位")
    @DeleteMapping
    @PreAuthorize("@el.check('qrcode:del')")
    public ResponseEntity<Object> deleteQrcode(@RequestBody Set<Long> ids){

        qrcodeService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}