package com.ntth.socialnetwork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "provinces")
public class Provinces {
	
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

	@Column(name = "administrative_unit_id", nullable = false)
	private Long administrativeUnitId;
	
	@Column(name = "administrative_region_id", nullable = false)
	private Long administrativeRegionId;

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

	public Long getAdministrativeUnitId() {
		return administrativeUnitId;
	}

	public void setAdministrativeUnitId(Long administrativeUnitId) {
		this.administrativeUnitId = administrativeUnitId;
	}

	public Long getAdministrativeRegionId() {
		return administrativeRegionId;
	}

	public void setAdministrativeRegionId(Long administrativeRegionId) {
		this.administrativeRegionId = administrativeRegionId;
	}

	public Provinces(String code, String nameEN, String fullName, String fullNameEN, String codeName,
			Long administrativeUnitId, Long administrativeRegionId) {
		super();
		this.code = code;
		this.nameEN = nameEN;
		this.fullName = fullName;
		this.fullNameEN = fullNameEN;
		this.codeName = codeName;
		this.administrativeUnitId = administrativeUnitId;
		this.administrativeRegionId = administrativeRegionId;
	}

	public Provinces() {
		super();
	}
	
	
	
}
