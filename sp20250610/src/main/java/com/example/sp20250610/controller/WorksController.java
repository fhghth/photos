package com.example.sp20250610.controller;

import com.example.sp20250610.common.Result;
import com.example.sp20250610.entity.Works;
import com.example.sp20250610.entity.WorkImage;
import com.example.sp20250610.service.WorkService;
import com.example.sp20250610.service.WorkImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/photos")
@CrossOrigin(origins = "*")
public class WorksController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Autowired
    private WorkService workService;

    @Autowired
    private WorkImageService workImageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("lng") String lng,
            @RequestParam("lat") String lat,
            @RequestParam("address") String address,
            @RequestParam("ctime") String ctime,
            @RequestParam("username") String username,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("coverIndex") String coverIndex) {
        
        try {
            // 1. 保存基本信息到works表
            Works work = new Works();
            work.setTitle(title);
            work.setCategory(category);
            work.setDescription(description);
            work.setLocationLng(new java.math.BigDecimal(lng));
            work.setLocationLat(new java.math.BigDecimal(lat));
            work.setLocationAddress(address);
            work.setCreatedAt(Timestamp.valueOf(ctime));
            work.setUsername(username);
            work.setRole("false"); // 默认审核状态为false

            // 2. 保存封面图片
            int coverIdx = Integer.parseInt(coverIndex);
            String coverFilename = saveImage(files[coverIdx]);
            work.setCover(coverFilename);

            // 3. 保存works记录
            work = workService.save(work);

            // 4. 保存封面图片到workimages表（单独记录）
            WorkImage coverWorkImage = new WorkImage();
            coverWorkImage.setWorkId(work.getId());
            coverWorkImage.setImagePath(coverFilename);
            coverWorkImage.setCover(true);
            workImageService.save(coverWorkImage);

            // 5. 保存其他图片到workimages表
            for (int i = 0; i < files.length; i++) {
                if (i != coverIdx) {
                    String imageFilename = saveImage(files[i]);
                    WorkImage workImage = new WorkImage();
                    workImage.setWorkId(work.getId());
                    workImage.setImagePath(imageFilename);
                    workImage.setCover(false);
                    workImageService.save(workImage);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("code", "200");
            response.put("msg", "上传成功");
            response.put("data", work);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", "500");
            response.put("msg", "上传失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/worksApprove")
    public Result getAllApprovedWorks(@RequestParam(value = "query", required = false) String query) {
        try {
            List<Works> approvedWorks = workService.findAllApprovedWorks(query);

            if (approvedWorks.isEmpty()) {
                return Result.success(approvedWorks);
            }

            // 2. 批量获取作品的图片
            Map<BigInteger, List<WorkImage>> workImagesMap = workImageService.getImagesForWorks(approvedWorks);

            // 3. 将图片数据关联到作品
            approvedWorks.forEach(work -> work.setImages(workImagesMap.getOrDefault(work.getId(), List.of())));

            return Result.success(approvedWorks);
        } catch (Exception e) {
            return Result.error("500", "获取审核通过的作品失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    public Result getWorkDetail(@PathVariable BigInteger id) {
        Works work = workService.findById(id);
        if (work == null) {
            return Result.error("404", "未找到该作品");
        }
        // 查询图片
        List<WorkImage> images = workImageService.findByWorkId(id);
        work.setImages(images);
        return Result.success(work);
    }

    //
    private String saveImage(MultipartFile file) throws IOException {
        // 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String newFilename = System.currentTimeMillis() + "_" +file.getOriginalFilename();

        // 保存文件
        Path filePath = Paths.get(uploadPath, newFilename);
        Files.copy(file.getInputStream(), filePath);

        return newFilename;
    }
}