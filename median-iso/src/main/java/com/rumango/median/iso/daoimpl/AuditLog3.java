package com.rumango.median.iso.daoimpl;
//package com.rumango.median.iso.entity;
//
//import java.sql.Timestamp;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Lob;
//import javax.persistence.Table;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//
//@Entity
//@Table(name = "median_audit_log")
//@JsonAutoDetect
//public class AuditLog3 {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private int id;
//
//	@Column(name = "external_system_name")
//	private String externalSystemName;
//
//	@Column(name = "request_ip")
//	private String ipAddress;
//
//	@Column(name = "request_status")
//	private String requestStatus;
//
//	@Column(name = "response_status")
//	private String responseStatus;
//
//	@Column(name = "modified_timestamp")
//	private Timestamp timeStamp;
//
//	@Column(name = "median_uuid")
//	private String medianUuid;
//	@Lob
//	@Column(name = "original_request", length = 10000)
//	private String original_request;
//
//	@Lob
//	@Column(name = "modified_request_isomsg", length = 10000)
//	private String modified_request_isomsg;
//
//	@Lob
//	@Column(name = "original_response_isomsg", length = 10000)
//	private String original_response_isomsg;
//
//	@Lob
//	@Column(name = "modified_response_isomsg", length = 10000)
//	private String modified_response_isomsg;
//
//	@Lob
//	@Column(name = "original_request_splitted", length = 10000)
//	private String original_request_splitted;
//
//	@Lob
//	@Column(name = "modified_request_splitted", length = 10000)
//	private String modified_request_splitted;
//
//	@Lob
//	@Column(name = "original_response_splitted", length = 10000)
//	private String original_response_splitted;
//
//	@Lob
//	@Column(name = "modified_response_splitted", length = 10000)
//	private String modified_response_splitted;
//
//}
