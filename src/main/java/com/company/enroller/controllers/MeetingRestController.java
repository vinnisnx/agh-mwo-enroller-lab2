package com.company.enroller.controllers;

import java.util.Collection;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingById(@PathVariable("id") long input) {
        Meeting meeting;
//        if (input.matches("\\d+")) {
//            long id = Long.parseLong(input);
        meeting = meetingService.getById(input);
//        } else {
//            meeting = meetingService.getByTitle(input);
//        }
//        if (meeting == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting) {
        meetingService.addMeeting(meeting);
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable long id) {
        if (meetingService.getById(id) == null) {
            return new ResponseEntity<>("Unable to delete. A meeting with id " + id + " doesn't exist.", HttpStatus.CONFLICT);
        }
        Meeting meeting = meetingService.getById(id);
        meetingService.deleteMeeting(id);
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value="", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@RequestBody Meeting meeting) {
        if (meetingService.getById(meeting.getId()) == null) {
            return new ResponseEntity<>("Unable to change. A meeting with ID " + meeting.getId() + " doesn't exist.", HttpStatus.CONFLICT);
        }
        meetingService.actualizeMeeting(meeting);
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }

}
