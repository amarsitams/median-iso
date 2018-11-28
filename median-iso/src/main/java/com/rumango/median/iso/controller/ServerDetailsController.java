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

import com.rumango.median.iso.dao.service.ServerDetailsService;
import com.rumango.median.iso.entity.ServerDetails;

@RestController
@CrossOrigin("*")
@RequestMapping("/iso")
public class ServerDetailsController {
	@Autowired
	private ServerDetailsService serverDetailsService;

	@PostMapping("/postserverdetails")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postServerDetails(@Valid @RequestBody ServerDetails serverDetails) {
		serverDetailsService.saveServerDetails(serverDetails);
	}

	@PostMapping("/postserverdetailslist")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postServerDetailsList(@Valid @RequestBody List<ServerDetails> list) {
		serverDetailsService.saveAllServerDetails(list);
	}

	@GetMapping("/getalldetails")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ServerDetails> getAllServerDetails() {
		return (List<ServerDetails>) serverDetailsService.getAllDetails();
	}

	@GetMapping("/getdetail/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ServerDetails getServerDetails(@PathVariable("id") Integer id) {
		return serverDetailsService.getDetailsById(id);
	}
}
