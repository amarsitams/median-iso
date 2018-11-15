package com.rumango.median.iso.controller;

import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@CrossOrigin("*")
@RequestMapping("/iso")
public class MainController {
	private final static Logger logger = Logger.getLogger(MainController.class);

	@GetMapping("/getresponse")
	@Produces(MediaType.APPLICATION_JSON)
	public String getresponse(String message) {
		logger.info("inside get response of MainController ");
		// StringIsoMessage isoMsg = convertor.getResponse(message);
		return "";
	}

	@GetMapping(value = { "/", "/login" })
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@GetMapping("/getprofile")
	public void getProfile() {
		// datasourceConfig.setup();
	}

//	@PostMapping("/sendsystemdetails")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public void getSystemdetails(@Valid @RequestBody ExternalSystemDetails systemDetails) {
//		this.externalSystemDetails = systemDetails;
//	}

//	@GetMapping("/getmandatoryfields")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Iso87Rules getFieldDetails() {
//		return iso87RulesRepository.findByFieldNo(49);
//	}

	@GetMapping("/convert")
	public void convertMessage(List<String> validations) {

	}

//	@GetMapping("/pack")
//	@Produces(MediaType.APPLICATION_JSON)
//	public StringIsoMessage packMessage() {
////		ISOMsg isoMsg = convertor.packMessage(isoMessage);
////		if (isoMsg == null)
////			throw new NullPointerException();
//		StringIsoMessage message = convertor.packMessage(isoMessage, null);
//		if (message == null)
//			throw new NullPointerException();
//		return message;
//	}

//	@PostMapping("/unpack")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Map<String, String> unpackMessage(@Valid @RequestBody StringIsoMessage message) {
//		isoMessage = convertor.unpackMessage(message.getMessage(), "ISO8583-1987");
//		if (isoMessage == null)
//			throw new NullPointerException();
//		return getMapOfIso(isoMessage);
//	}
//
//	@PostMapping("/response")
//	@Produces(MediaType.APPLICATION_JSON)
//	public StringIsoMessage getresponse(@Valid @RequestBody StringIsoMessage message) {
//		logger.info("inside get response of MainController ");
//		StringIsoMessage isoMsg = convertor.getResponse(message);
//		if (isoMsg == null)
//			throw new NullPointerException();
//		return isoMsg;
//	}

//	private Map<String, String> getMapOfIso(@Valid @NotNull ISOMsg msg) {
//		Map<String, String> map = new LinkedHashMap<>();
//		try {
//			map.put("MTI", msg.getMTI());
//			for (int i = 1; i <= msg.getMaxField(); i++) {
//				if (msg.hasField(i)) {
//					map.put(i + "  ", msg.getString(i));
//				}
//			}
//		} catch (Exception e) {
//			return null;
//		}
//		return map;
//	}
}
