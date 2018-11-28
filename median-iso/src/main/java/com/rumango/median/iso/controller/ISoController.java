package com.rumango.median.iso.controller;

import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumango.median.iso.service.GetResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/iso")
public class ISoController {

//	@Autowired
//	private Iso87LovService lovService;

	@Autowired
	private GetResponse response;

	@PostMapping("/postiso")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postISO(@Valid @RequestBody Map<String, String> isoMsg) {
		ISOMsg m = new ISOMsg();
		for (Map.Entry<String, String> entry : isoMsg.entrySet()) {
			System.out.println("KEY : " + entry.getKey() + " VALUE " + entry.getValue());
			try {
				// Integer.parseInt(entry.getKey())
				m.set(Integer.parseInt(entry.getKey()), entry.getValue());
			} catch (Exception e) {
				e.getMessage();
				m.set(0, entry.getValue());
			}
		}
		System.out.println(response.convertAndRespond(null, null));
		// response.convertAndRespond(m, null);

	}

//	@GetMapping("/getisoresponse")
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<LovRule> getAllRules() {
//		return lovService.getAllRules();
//	}

//	@PostMapping("/postlovrules")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public void postAllISO87Rules(@Valid @RequestBody List<LovRule> rulesList) {
//		lovService.saveAllISO87Lovs(rulesList);
//	}
//
//	@GetMapping("/getlovvalues/{fieldNumber}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<String> getLovValues(@PathVariable("fieldNumber") Integer fieldNumber) {
//		return lovService.getAllRules(fieldNumber);
//	}
}
