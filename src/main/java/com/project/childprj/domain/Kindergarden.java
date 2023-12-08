package com.project.childprj.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kindergarden {
    private Long id;
    private String KINDERNAME;
    private String ESTABLISH;
    private String LDGRNAME;
    private Date ODATE;
    private String ADDR;
    private String TELNO;
    private String HPADDR;
    private String OPERTIME;
}