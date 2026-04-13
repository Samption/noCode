package com.sxl.nocode.langgraph4j.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.sxl.nocode.exception.BusinessException;
import com.sxl.nocode.exception.ErrorCode;
import com.sxl.nocode.langgraph4j.model.ImageResource;
import com.sxl.nocode.langgraph4j.model.enums.ImageCategoryEnum;
import com.sxl.nocode.manager.CosManager;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mermaid 架构图生成工具
 */
@Slf4j
@Component
public class MermaidDiagramTool {

    @Resource
    private CosManager cosManager;
    
    @Tool("将 Mermaid 代码转换为架构图图片，用于展示系统结构和技术关系")
    public List<ImageResource> generateMermaidDiagram(@P("Mermaid 图表代码") String mermaidCode,
                                                      @P("架构图描述") String description) {
        if (StrUtil.isBlank(mermaidCode)) {
            return new ArrayList<>();
        }
        try {
            // 转换为SVG图片
            File diagramFile = convertMermaidToSvg(mermaidCode);
            // 上传到COS
            String keyName = String.format("/mermaid/%s/%s",
                    RandomUtil.randomString(5), diagramFile.getName());
            String cosUrl = cosManager.uploadFile(keyName, diagramFile);
            // 清理临时文件
            FileUtil.del(diagramFile);
            if (StrUtil.isNotBlank(cosUrl)) {
                return Collections.singletonList(ImageResource.builder()
                        .category(ImageCategoryEnum.ARCHITECTURE)
                        .description(description)
                        .url(cosUrl)
                        .build());
            }
        } catch (Exception e) {
            log.error("生成架构图失败: {}", e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    /**
     * 将Mermaid代码转换为SVG图片
     */
    private File convertMermaidToSvg(String mermaidCode) throws IOException, InterruptedException {
        // 创建临时输入文件
        File tempInputFile = FileUtil.createTempFile("mermaid_input_", ".mmd", true);
        FileUtil.writeUtf8String(mermaidCode, tempInputFile);
        // 创建临时输出文件
        File tempOutputFile = FileUtil.createTempFile("mermaid_output_", ".svg", true);
        // 根据操作系统选择命令
        String command = SystemUtil.getOsInfo().isWindows() ? "cmd.exe /c mmdc" : "mmdc";
        // 构建命令
        List<String> commandList = new ArrayList<>();
        if (SystemUtil.getOsInfo().isWindows()) {
            commandList.add("cmd.exe");
            commandList.add("/c");
        }
        commandList.add("mmdc");
        commandList.add("-i");
        commandList.add(tempInputFile.getAbsolutePath());
        commandList.add("-o");
        commandList.add(tempOutputFile.getAbsolutePath());
        commandList.add("-b");
        commandList.add("transparent");

        ProcessBuilder pb = new ProcessBuilder(commandList);
        // 执行命令
        // 将 mmdc 的错误输出合并到标准输出，防止进程因为错误流堆积而卡死
        pb.redirectErrorStream(true);

        // 启动进程
        Process process = pb.start();

        // 阻塞等待，直到命令执行完毕
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Mermaid CLI 执行失败，退出码：" + exitCode);
        }

        // 检查输出文件
        if (!tempOutputFile.exists() || tempOutputFile.length() == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Mermaid CLI 执行失败");
        }

        // 清理输入文件
        FileUtil.del(tempInputFile);
        return tempOutputFile;
    }
}
