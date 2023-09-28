package com.techbank.account.cmd.api.controller;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techbank.account.cmd.api.commands.WithdrawFundsCommand;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;

@RestController
@RequestMapping(path = "/api/v1/withdrawFunds")
public class WithdrawFundsController {

	private final Logger logger = Logger.getLogger(WithdrawFundsController.class.getName());

	@Autowired
	private CommandDispatcher commandDispatcher;

	@PutMapping(path = "/{id}")
	public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable String id, @RequestBody WithdrawFundsCommand command) {
		try {
			command.setId(id);
			commandDispatcher.send(command);
			logger.log(Level.INFO, "withraw funds with id  " + id);
			return new ResponseEntity<>(new BaseResponse("Funds withdraw request completed successfully"),
					HttpStatus.OK);
		} catch (IllegalStateException e) {
			logger.log(Level.WARNING, MessageFormat.format("Client made a bad reqeust {0}", e.toString()));
			return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.log(Level.SEVERE, MessageFormat.format("Error while process withdraw fund request for id = {0}", id));
			return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
