package com.rumango.median.iso.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Entity
@Table(name = "median_iso_field_types")
@JsonAutoDetect
public class IsoFiledTypes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "field_number")
	private int fieldNumber;

	@Column(name = "message_type")
	private String messageType;

	@Column(name = "message_version")
	private String messageVersion;

	@Column(name = "data_type")
	private String dataType;

	@Column(name = "field_length")
	private int fieldLength;

	// Msg_Type, Msg_Version, Fields, DataType, Data_Length

}
