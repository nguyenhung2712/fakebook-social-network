package com.ntth.socialnetwork.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "location")
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "location_id", nullable = false)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "province_code", nullable = false)
	private Provinces province;
	
	@ManyToOne
	@JoinColumn(name = "district_code", nullable = false)
	private Districts district;
	
	@ManyToOne
	@JoinColumn(name = "ward_code", nullable = false)
	private Wards ward;
	
	@Column(name = "address", nullable = false)
	private String address;
	
	@JsonIgnore
  	@OneToMany(mappedBy="location")
  	private Set<UserProfile> profiles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Provinces getProvince() {
		return province;
	}

	public void setProvince(Provinces province) {
		this.province = province;
	}

	public Districts getDistrict() {
		return district;
	}

	public void setDistrict(Districts district) {
		this.district = district;
	}

	public Wards getWard() {
		return ward;
	}

	public void setWard(Wards ward) {
		this.ward = ward;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Location() {
		super();
	}

	public Location(Provinces province, Districts district, Wards ward, String address) {
		super();
		this.province = province;
		this.district = district;
		this.ward = ward;
		this.address = address;
	}
	
}