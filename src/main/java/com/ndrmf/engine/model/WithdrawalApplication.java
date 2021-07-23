package com.ndrmf.engine.model;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "withdrawal_application")
public class WithdrawalApplication {
	private UUID Id;	//withdrawal Application No.
	private GrantImplementationAgreement gia;
	//1.
	private String disbursementType;
	//2.
	private String applicationCurrency;
	private float applicationAmount;
	private String applicationAmountInWords;
	//3.
	private String payeesName;
	private String payeesAddress;
	private String payeesBankName;
	private String payeesBankAddress;
	private String payeesAccountNo;
	private String specialInstruciton;
	//4.
	//private List<String> applicationConsistsOf;
	//5. Terms and conditions to be shown on the front-end only, nothing to store here.
	private String nameOfRecipient;
	private LocalDate dateSigned;
	private String nameOfAuthorizedSignatory1;
	private String nameOfAuthorizedSignatory2;
	//Contact details
	private String contactName;
	private String contactPhone;
	private String contactEmailAddress;
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	public UUID getId() {
		return Id;
	}
	public void setId(UUID id) {
		Id = id;
	}
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gia_id")
	public GrantImplementationAgreement getGia() {
		return gia;
	}
	public void setGia(GrantImplementationAgreement gia) {
		this.gia = gia;
	}
	public String getDisbursementType() {
		return disbursementType;
	}
	public void setDisbursementType(String disbursementType) {
		this.disbursementType = disbursementType;
	}
	public String getApplicationCurrency() {
		return applicationCurrency;
	}
	public void setApplicationCurrency(String applicationCurrency) {
		this.applicationCurrency = applicationCurrency;
	}
	public float getApplicationAmount() {
		return applicationAmount;
	}
	public void setApplicationAmount(float applicationAmount) {
		this.applicationAmount = applicationAmount;
	}
	public String getApplicationAmountInWords() {
		return applicationAmountInWords;
	}
	public void setApplicationAmountInWords(String applicationAmountInWords) {
		this.applicationAmountInWords = applicationAmountInWords;
	}
	public String getPayeesName() {
		return payeesName;
	}
	public void setPayeesName(String payeesName) {
		this.payeesName = payeesName;
	}
	public String getPayeesAddress() {
		return payeesAddress;
	}
	public void setPayeesAddress(String payeesAddress) {
		this.payeesAddress = payeesAddress;
	}
	public String getPayeesBankName() {
		return payeesBankName;
	}
	public void setPayeesBankName(String payeesBankName) {
		this.payeesBankName = payeesBankName;
	}
	public String getPayeesBankAddress() {
		return payeesBankAddress;
	}
	public void setPayeesBankAddress(String payeesBankAddress) {
		this.payeesBankAddress = payeesBankAddress;
	}
	public String getPayeesAccountNo() {
		return payeesAccountNo;
	}
	public void setPayeesAccountNo(String payeesAccountNo) {
		this.payeesAccountNo = payeesAccountNo;
	}
	public String getSpecialInstruciton() {
		return specialInstruciton;
	}
	public void setSpecialInstruciton(String specialInstruciton) {
		this.specialInstruciton = specialInstruciton;
	}
	
	/*@OneToMany(mappedBy="applicationConsistsOf", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	public List<String> getApplicationConsistsOf() {
		return applicationConsistsOf;
	}
	public void setApplicationConsistsOf(List<String> applicationConsistsOf) {
		this.applicationConsistsOf = applicationConsistsOf;
	}
	*/
	public String getNameOfRecipient() {
		return nameOfRecipient;
	}
	public void setNameOfRecipient(String nameOfRecipient) {
		this.nameOfRecipient = nameOfRecipient;
	}
	public LocalDate getDateSigned() {
		return dateSigned;
	}
	public void setDateSigned(LocalDate dateSigned) {
		this.dateSigned = dateSigned;
	}
	public String getNameOfAuthorizedSignatory1() {
		return nameOfAuthorizedSignatory1;
	}
	public void setNameOfAuthorizedSignatory1(String nameOfAuthorizedSignatory1) {
		this.nameOfAuthorizedSignatory1 = nameOfAuthorizedSignatory1;
	}
	public String getNameOfAuthorizedSignatory2() {
		return nameOfAuthorizedSignatory2;
	}
	public void setNameOfAuthorizedSignatory2(String nameOfAuthorizedSignatory2) {
		this.nameOfAuthorizedSignatory2 = nameOfAuthorizedSignatory2;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactEmailAddress() {
		return contactEmailAddress;
	}
	public void setContactEmailAddress(String contactEmailAddress) {
		this.contactEmailAddress = contactEmailAddress;
	}

}
