package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author gakki
 */
@Api(value = "文件管理系统", description = "文件管理接口，提供页面的增、删、改、查")
public interface FileSystemControllerApi {

    @ApiOperation("上传文件接口")
    public UploadFileResult upload(MultipartFile multipartFile,
                                   String fileTag,
                                   String businessKey,
                                   String metadata);
}
