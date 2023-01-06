package com.ntth.socialnetwork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "districts")
public class Districts {

    @Id
	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "name_en", nullable = false)
	private String nameEN;
	
	@Column(name = "full_name", nullable = false)
	private String fullName;
	
	@Column(name = "full_name_en", nullable = false)
	private String fullNameEN;
	
	@Column(name = "code_name", nullable = false)
	private String codeName;

	@Column(name = "province_code", nullable = false)
	private Long provinceCode;
	
	@Column(name = "administrative_unit_id", nullable = false)
	private Long administrativeUnitId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullNameEN() {
		return fullNameEN;
	}

	public void setFullNameEN(String fullNameEN) {
		this.fullNameEN = fullNameEN;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public Long getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(Long provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Long getAdministrativeUnitId() {
		return administrativeUnitId;
	}

	public void setAdministrativeUnitId(Long administrativeUnitId) {
		this.administrativeUnitId = administrativeUnitId;
	}

	public Districts(String code, String nameEN, String fullName, String fullNameEN, String codeName,
			Long provinceCode, Long administrativeUnitId) {
		super();
		this.code = code;
		this.nameEN = nameEN;
		this.fullName = fullName;
		this.fullNameEN = fullNameEN;
		this.codeName = codeName;
		this.provinceCode = provinceCode;
		this.administrativeUnitId = administrativeUnitId;
	}

	public Districts() {
		super();
	}
	
	
	
}
