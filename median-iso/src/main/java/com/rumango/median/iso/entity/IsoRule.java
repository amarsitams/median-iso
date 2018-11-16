package com.rumango.median.iso.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Entity
@Table(name = "median_iso_rule")
@JsonAutoDetect
public class IsoRule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String externlSystem;

	private String targetSystem;

	private int fieldNumber;

	private String fieldDescription;

	private String rule;

	private String defaultValue;

	private String recordStatus;

	private String authStatus;

	private String maker;

	private String checker;

	private Timestamp makerTimestamp;

	private Timestamp checkerTimestamp;

	private int modNo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExternlSystem() {
		return externlSystem;
	}

	public void setExternlSystem(String externlSystem) {
		this.externlSystem = externlSystem;
	}

	public String getTargetSystem() {
		return targetSystem;
	}

	public void setTargetSystem(String targetSystem) {
		this.targetSystem = targetSystem;
	}

	public int getFieldNumber() {
		return fieldNumber;
	}

	public void setFieldNumber(int fieldNumber) {
		this.fieldNumber = fieldNumber;
	}

	public String getFieldDescription() {
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public Timestamp getMakerTimestamp() {
		return makerTimestamp;
	}

	public void setMakerTimestamp(Timestamp makerTimestamp) {
		this.makerTimestamp = makerTimestamp;
	}

	public Timestamp getCheckerTimestamp() {
		return checkerTimestamp;
	}

	public void setCheckerTimestamp(Timestamp checkerTimestamp) {
		this.checkerTimestamp = checkerTimestamp;
	}

	public int getModNo() {
		return modNo;
	}

	public void setModNo(int modNo) {
		this.modNo = modNo;
	}

}
