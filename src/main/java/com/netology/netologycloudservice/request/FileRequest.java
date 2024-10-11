package com.netology.netologycloudservice.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FileRequest {
    @NotNull
    private String filename;

    @NotNull
    private String filePath;
}
