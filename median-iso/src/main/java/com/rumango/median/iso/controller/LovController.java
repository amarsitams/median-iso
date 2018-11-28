package com.rumango.median.iso.controller;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumango.median.iso.dao.service.Iso87LovService;
import com.rumango.median.iso.entity.LovRule;

@RestController
@CrossOrigin("*")
@RequestMapping("/iso")
public class LovController {
	@Autowired
	private Iso87LovService lovService;

	@PostMapping("/postlovrule")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postISO87Rules(@Valid @RequestBody LovRule iso87Rule) {
		lovService.saveISO87Lov(iso87Rule);
	}

	@PostMapping("/postlovrules")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postAllISO87Rules(@Valid @RequestBody List<LovRule> rulesList) {
		lovService.saveAllISO87Lovs(rulesList);
	}

	@GetMapping("/getlovrules")
	@Produces(MediaType.APPLICATION_JSON)
	public List<LovRule> getAllRules() {
		return lovService.getAllRules();
	}

	@GetMapping("/getlovvalues/{fieldNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getLovValues(@PathVariable("fieldNumber") Integer fieldNumber) {
		return lovService.getAllRules(fieldNumber);
	}
}
