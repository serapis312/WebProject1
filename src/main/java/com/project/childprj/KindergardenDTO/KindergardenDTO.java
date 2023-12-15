package com.project.childprj.KindergardenDTO;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// todo: jsonparser로 json 을 가져와서 json으로 parsing
// 필요한 파라미터만 가져와서 데이터베이스에 저장해야함
public class KindergardenDTO {
//    private Long id;
@JsonProperty("KINDERNAME")
private String kinderName;

    @JsonProperty("ESTABLISH")
    private String establish;

    @JsonProperty("HPADDR")
    private String hpaddr;

    @JsonProperty("LDGRNAME")
    private String ldgrName;

    @JsonProperty("ADDR")
    private String addr;

    @JsonProperty("TELNO")
    private String telNo;

    @JsonProperty("OPERTIME")
    private String operTime;

    public String getKinderName() {
        return kinderName;
    }

    public void setKinderName(String kinderName) {
        this.kinderName = kinderName;
    }

    public String getEstablish() {
        return establish;
    }

    public void setEstablish(String establish) {
        this.establish = establish;
    }

    public String getHpaddr() {
        return hpaddr;
    }

    public void setHpaddr(String hpaddr) {
        this.hpaddr = hpaddr;
    }

    public String getLdgrName() {
        return ldgrName;
    }

    public void setLdgrName(String ldgrName) {
        this.ldgrName = ldgrName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    
    // api의 rows에서 객체의 각각 필드에 설정
    public static KindergardenDTO fromJson(JsonNode row) {
    	KindergardenDTO kindergardenDTO = new KindergardenDTO();
		kindergardenDTO.setKinderName(row.get("KINDERNAME").asText());
		kindergardenDTO.setEstablish(row.get("ESTABLISH").asText());
		kindergardenDTO.setHpaddr(row.get("HPADDR").asText());
		kindergardenDTO.setLdgrName(row.get("LDGRNAME").asText());
		kindergardenDTO.setAddr(row.get("ADDR").asText());
		kindergardenDTO.setTelNo(row.get("TELNO").asText());
		kindergardenDTO.setOperTime(row.get("OPERTIME").asText());

		
    	return kindergardenDTO;
    }
   
}
