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

import com.rumango.median.iso.dao.service.Iso87RulesService;
import com.rumango.median.iso.entity.IsoRule;

@RestController
@CrossOrigin("*")
@RequestMapping("/iso")
public class RulesController {
	@Autowired
	private Iso87RulesService iso87RulesService;

	@PostMapping("/postiso87rule")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postISO87Rules(@Valid @RequestBody IsoRule iso87Rule) {
		iso87RulesService.saveISO87Rule(iso87Rule);
	}

	@PostMapping("/postiso87rules")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postAllISO87Rules(@Valid @RequestBody List<IsoRule> rulesList) {
		iso87RulesService.saveAllISO87Rules(rulesList);
	}

	@GetMapping("/getall87rules")
	@Produces(MediaType.APPLICATION_JSON)
	public List<IsoRule> getAllRules() {
		return (List<IsoRule>) iso87RulesService.getAllRules();
	}

	@GetMapping("/get87rule/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public IsoRule getRule(@PathVariable("id") Integer id) {
		return iso87RulesService.findByFieldNumber(id);
	}

	@GetMapping("/add87rule")
	@Produces(MediaType.APPLICATION_JSON)
	public void addRule() {
		// iso87RulesService.addRule();
	}
}
