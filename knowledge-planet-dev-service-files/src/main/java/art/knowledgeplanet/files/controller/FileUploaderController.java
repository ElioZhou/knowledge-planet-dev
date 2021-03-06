package art.knowledgeplanet.files.controller;

import art.knowledgeplanet.api.controller.files.FileUploaderControllerApi;
import art.knowledgeplanet.exception.GraceException;
import art.knowledgeplanet.files.resource.FileResource;
import art.knowledgeplanet.files.service.UploaderService;
import art.knowledgeplanet.grace.result.GraceJSONResult;
import art.knowledgeplanet.grace.result.ResponseStatusEnum;
import art.knowledgeplanet.pojo.bo.NewAdminBO;
//import art.knowledgeplanet.utils.FileUtils;
import art.knowledgeplanet.utils.extend.AliImageReviewUtils;
//import com.mongodb.client.gridfs.GridFSBucket;
//import com.mongodb.client.gridfs.GridFSFindIterable;
//import com.mongodb.client.gridfs.model.GridFSFile;
//import com.mongodb.client.model.Filters;
//import com.mongodb.gridfs.GridFS;
import org.apache.commons.lang3.StringUtils;
//import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileUploaderController implements FileUploaderControllerApi {

    final static Logger logger = LoggerFactory.getLogger(FileUploaderController.class);

    @Autowired
    private UploaderService uploaderService;

    @Autowired
    private FileResource fileResource;

    @Autowired
    private AliImageReviewUtils aliImageReviewUtils;

//    @Autowired
//    private GridFSBucket gridFSBucket;

    @Override
    public GraceJSONResult uploadFace(String userId,
                                      MultipartFile file) throws Exception {

        String path = "";
        if (file != null) {
            // ???????????????????????????
            String fileName = file.getOriginalFilename();

            // ???????????????????????????
            if (StringUtils.isNotBlank(fileName)) {
                String fileNameArr[] = fileName.split("\\.");
                // ????????????
                String suffix = fileNameArr[fileNameArr.length - 1];
                // ??????????????????????????????????????????
                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")
                ) {
                    return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }

                // ????????????
//                path = uploaderService.uploadFdfs(file, suffix);
                path = uploaderService.uploadOSS(file, userId, suffix);

            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
            }
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }

        logger.info("path = " + path);

        String finalPath = "";
        if (StringUtils.isNotBlank(path)) {
//            finalPath = fileResource.getHost() + path;
            finalPath = fileResource.getOssHost() + path;
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
//        return GraceJSONResult.ok(doAliImageReview(finalPath));
        return GraceJSONResult.ok(finalPath);
    }

    @Override
    public GraceJSONResult uploadSomeFiles(String userId,
                                           MultipartFile[] files)
            throws Exception {


        // ??????list????????????????????????????????????????????????????????????
        List<String> imageUrlList = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                String path = "";
                if (file != null) {
                    // ???????????????????????????
                    String fileName = file.getOriginalFilename();

                    // ???????????????????????????
                    if (StringUtils.isNotBlank(fileName)) {
                        String fileNameArr[] = fileName.split("\\.");
                        // ????????????
                        String suffix = fileNameArr[fileNameArr.length - 1];
                        // ??????????????????????????????????????????
                        if (!suffix.equalsIgnoreCase("png") &&
                                !suffix.equalsIgnoreCase("jpg") &&
                                !suffix.equalsIgnoreCase("jpeg")
                        ) {
                            continue;
                        }

                        // ????????????
//                        path = uploaderService.uploadFdfs(file, suffix);
                        path = uploaderService.uploadOSS(file, userId, suffix);

                    } else {
                        continue;
                    }
                } else {
                    continue;
                }

                String finalPath = "";
                if (StringUtils.isNotBlank(path)) {
//                    finalPath = fileResource.getHost() + path;
                    finalPath = fileResource.getOssHost() + path;
                    // FIXME: ?????????imagelist???????????????????????????????????????
                    imageUrlList.add(finalPath);
                } else {
                    continue;
                }
            }
        }

        return GraceJSONResult.ok(imageUrlList);
    }

    public static final String FAILED_IMAGE_URL = "https://imooc-news.oss-cn-shanghai.aliyuncs.com/image/faild.jpeg";
    private String doAliImageReview(String pendingImageUrl) {

        /**
         * fastdfs ?????????????????????????????????????????????????????????????????????
         * ??????????????????????????????
         * 1. ???????????????natppp/?????????/ngrok
         * 2. ????????????????????????
         * 3. fdfs ?????????????????????
         */
        boolean result = false;
        try {
            result = aliImageReviewUtils.reviewImage(pendingImageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!result) {
            return FAILED_IMAGE_URL;
        }

        return pendingImageUrl;
    }

//    @Override
//    public GraceJSONResult uploadToGridFS(NewAdminBO newAdminBO)
//            throws Exception {
//
//        // ???????????????base64?????????
//        String file64 = newAdminBO.getImg64();
//
//        // ???base64??????????????????byte??????
//        byte[] bytes = new BASE64Decoder().decodeBuffer(file64.trim());
//
//        // ??????????????????
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
//
//        // ?????????gridfs???
//        ObjectId fileId = gridFSBucket.uploadFromStream(newAdminBO.getUsername() + ".png", inputStream);
//
//        // ???????????????gridfs????????????id
//        String fileIdStr = fileId.toString();
//
//        return GraceJSONResult.ok(fileIdStr);
//    }
//
//    @Override
//    public void readInGridFS(String faceId,
//                                        HttpServletRequest request,
//                                        HttpServletResponse response) throws Exception {
//
//        // 0. ????????????
//        if (StringUtils.isBlank(faceId) || faceId.equalsIgnoreCase("null")) {
//            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
//        }
//
//        // 1. ???gridfs?????????
//        File adminFace = readGridFSByFaceId(faceId);
//
//        // 2. ?????????????????????????????????
//        FileUtils.downloadFileByStream(response, adminFace);
//    }
//
//    private File readGridFSByFaceId(String faceId) throws Exception {
//
//        GridFSFindIterable gridFSFiles
//                = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));
//
//        GridFSFile gridFS = gridFSFiles.first();
//
//        if (gridFS == null) {
//            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
//        }
//
//        String fileName = gridFS.getFilename();
//        System.out.println(fileName);
//
//        // ?????????????????????????????????????????????????????????????????????
//        File fileTemp = new File("/workspace/temp_face");
//        if (!fileTemp.exists()) {
//            fileTemp.mkdirs();
//        }
//
//        File myFile = new File("/workspace/temp_face/" + fileName);
//
//        // ?????????????????????
//        OutputStream os = new FileOutputStream(myFile);
//        // ??????????????????????????????
//        gridFSBucket.downloadToStream(new ObjectId(faceId), os);
//
//        return myFile;
//    }
//
//    @Override
//    public GraceJSONResult readFace64InGridFS(String faceId,
//                                              HttpServletRequest request,
//                                              HttpServletResponse response)
//            throws Exception {
//
//        // 0. ??????gridfs???????????????
//        File myface = readGridFSByFaceId(faceId);
//
//        // 1. ???????????????base64
//        String base64Face = FileUtils.fileToBase64(myface);
//
//        return GraceJSONResult.ok(base64Face);
//    }
}
