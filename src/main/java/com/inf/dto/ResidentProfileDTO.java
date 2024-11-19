package com.inf.dto;

public class ResidentProfileDTO {
    private String name;
    private String phoneNumber;
    private String societyName;
    private String flatNumber;
    private String postalCode;

	public ResidentProfileDTO(String name, String phoneNumber, String societyName, String flatNumber, String postalCode) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.societyName = societyName;
		this.flatNumber = flatNumber;
		this.postalCode = postalCode;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getSocietyName() {
		return societyName;
	}
	public void setSocietyName(String societyName) {
		this.societyName = societyName;
	}
	public String getFlatNumber() {
		return flatNumber;
	}
	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

    // Getters and setters...
    
    
}
