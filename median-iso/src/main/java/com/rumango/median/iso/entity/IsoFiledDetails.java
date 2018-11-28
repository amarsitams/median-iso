package com.rumango.median.iso.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Entity
@Table(name = "median_iso_field_details")
@JsonAutoDetect
public class IsoFiledDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "message_type")
	private String messageType;

	@Column(name = "message_version")
	private String messageVersion;

	@Column(name = "field_number")
	private int fieldNumber;

	@Column(name = "data_type")
	private String dataType;

	@Column(name = "data_length")
	private int dataLength;

	@Column(name = "field_description")
	private int fieldDescription;

	// Msg_Type, Msg_Version, Fields, DataType, Data_Length
	// Msg_Type, Msg_Version, Fields, DataType, Data_Length
	// Msg_Type => ISO-8583 , Msg_Version => 1993

}
