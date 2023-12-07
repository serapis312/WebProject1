package com.project.childprj.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildHouse {
    private Long id;
    private String SIGUNNAME;
    private String CRNAME;
    private String CRTYPENAME;
    private String CRSTATUSNAME;
    private String CRADDR;
    private String CRTELNO;
    private String CRHOME;
    private Integer NRTRROOMCNT;
    private Integer PLGRDCO;
    private Integer CCTVINSTLCNT;
    private Integer CHCRTESCNT;
    private Integer CRCAPAT;
    private Integer CRCHCNT;
    private Double LA;
    private Double LO;
    private String CRCARGBNAME;
}
