package com.rumango.median.iso.controller;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumango.median.iso.dao.service.IsoFieldDetailsService;
import com.rumango.median.iso.entity.IsoFiledDetails;

@RestController
@CrossOrigin("*")
@RequestMapping("/iso")
public class IsoFieldDetailsController {

	@Autowired
	private IsoFieldDetailsService service;

	@PostMapping("/postfield")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postISOField(@Valid @RequestBody IsoFiledDetails isoMsg) {
		return null;
		// service.saveISO87Lov(iso87Rule);
	}

	@PostMapping("/postfields")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postAllISOFields(@Valid @RequestBody List<IsoFiledDetails> rulesList) {
		// lovService.saveAllISO87Lovs(rulesList);
	}

//	@GetMapping("/getlovrules")
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<LovRule> getAllRules() {
//		return lovService.getAllRules();
//	}
//
//	@GetMapping("/getlovvalues/{fieldNumber}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<String> getLovValues(@PathVariable("fieldNumber") Integer fieldNumber) {
//		return lovService.getAllRules(fieldNumber);
//	}
}
