package pl.battlegrid.log;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LogDTO {

    private String command;
    private LocalDateTime createdAt;
}
