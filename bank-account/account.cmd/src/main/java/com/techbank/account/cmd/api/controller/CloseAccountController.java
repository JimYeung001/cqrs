package com.techbank.account.cmd.api.controller;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;

@RestController
@RequestMapping(path = "/api/v1/closeAccount")
public class CloseAccountController {

	private final Logger logger = Logger.getLogger(CloseAccountController.class.getName());

	@Autowired
	private CommandDispatcher commandDispatcher;

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<BaseResponse> depositFunds(@PathVariable String id) {
		try {
			commandDispatcher.send(new CloseAccountCommand(id));
			logger.log(Level.INFO, "Bank account closed, id= " + id);
			return new ResponseEntity<>(new BaseResponse("Account close successfully"), HttpStatus.OK);
		} catch (IllegalStateException e) {
			logger.log(Level.WARNING, MessageFormat.format("Client made a bad reqeust {0}", e.toString()));
			return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					MessageFormat.format("Error while process close account request for id = {0}", id));
			return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
