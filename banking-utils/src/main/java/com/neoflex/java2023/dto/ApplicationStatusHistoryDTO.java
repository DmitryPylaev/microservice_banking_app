package com.neoflex.java2023.dto;

import com.neoflex.java2023.enums.ApplicationStatus;
import com.neoflex.java2023.enums.ChangeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "История изменения статусов заявки")
public class ApplicationStatusHistoryDTO {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
