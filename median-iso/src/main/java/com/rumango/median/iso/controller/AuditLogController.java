package com.rumango.median.iso.controller;

import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumango.median.iso.dao.service.AuditLogService;
import com.rumango.median.iso.entity.AuditLog;

@RestController
@CrossOrigin("*")
@RequestMapping("/iso")
public class AuditLogController {
	@Autowired
	private AuditLogService auditLogService;

//	@PostMapping("/postiso87rule")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public void postISO87Rules(@Valid @RequestBody IsoRule iso87Rule) {
//		auditLogService.saveISO87Rule(iso87Rule);
//	}
//
//	@PostMapping("/postiso87rules")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public void postAllISO87Rules(@Valid @RequestBody List<IsoRule> rulesList) {
//		auditLogService.saveAllISO87Rules(rulesList);
//	}

	@GetMapping("/getlogs")
	@Produces(MediaType.APPLICATION_JSON)
	public List<AuditLog> getAllLogs() {
		return (List<AuditLog>) auditLogService.getAllLogs();
	}

	@GetMapping("/getlogip/{ip}")
	@Produces(MediaType.APPLICATION_JSON)
	public AuditLog getLogByIp(@PathVariable("ip") String ip) {
		return auditLogService.findByIp(ip);
	}

	@GetMapping("/getlog/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public AuditLog getLogById(@PathVariable("id") Integer id) {
		System.out.println("ID :" + id);
		return auditLogService.findById(id);
	}
}
