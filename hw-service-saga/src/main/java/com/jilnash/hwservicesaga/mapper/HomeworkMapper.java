package com.jilnash.hwservicesaga.mapper;

import com.jilnash.hwservicedto.dto.HomeworkCreateDTO;
import com.jilnash.hwservicesaga.dto.HomeworkCreateSagaDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class HomeworkMapper {

    public HomeworkCreateDTO homeworkCreateDTO(HomeworkCreateSagaDTO homeworkCreateDTO) {
        return new HomeworkCreateDTO(
                homeworkCreateDTO.getTaskId(),
                homeworkCreateDTO.getStudentId(),
                homeworkCreateDTO.getFiles().stream().map(MultipartFile::getOriginalFilename).toList()
        );
    }
}
