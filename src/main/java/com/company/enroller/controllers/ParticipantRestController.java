package com.company.enroller.controllers;

import java.util.Collection;

import com.company.enroller.persistence.DatabaseConnector;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@RequestParam(value = "sortOrder", defaultValue = "") String order, @RequestParam(value = "key", defaultValue = "") String filter){
		Collection<Participant> participants = participantService.getAll(order, filter);
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		String hashedPassword = passwordEncoder.encode(participant.getPassword());
		participant.setPassword(hashedPassword);
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
		}
		participantService.addParticipant(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@RequestBody Participant participant) {
		String login = participant.getLogin();
		if (participantService.findByLogin(login) == null) {
			return new ResponseEntity("Unable to delete. A participant with login " + login + " doesn't exist.", HttpStatus.CONFLICT);
		}
		participantService.deleteParticipant(login);
		return new ResponseEntity(login, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResponseEntity<?> changeParticipant(@RequestBody Participant participant) {
		String login = participant.getLogin();
		if (participantService.findByLogin(participant.getLogin()) == null) {
			return new ResponseEntity("Unable to change. A participant with login " + login + " doesn't exist.", HttpStatus.CONFLICT);
		}
		String hashedPassword = passwordEncoder.encode(participant.getPassword());
		participant.setPassword(hashedPassword);
		participantService.actualizeParticipant(participant);
		return new ResponseEntity(participant, HttpStatus.OK);
	}
}
