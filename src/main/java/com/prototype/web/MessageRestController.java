package com.prototype.web;

import com.prototype.model.Address;
import com.prototype.model.User;
import com.prototype.model.Message;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.address.AddressService;
import com.prototype.service.message.MessageServiceImp;
import com.prototype.service.user.UserService;
import com.prototype.to.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = MessageRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageRestController {
    static final String REST_URL = "/api/v1";

    @Autowired
    private MessageServiceImp messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

//    @PostMapping(value = "/me/chat/{addressId}/{number}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<Message>> createMessage(@PathVariable("addressId") BigInteger addressId, @PathVariable("number") BigInteger number, @RequestBody UserMessage message) {
//        if (message == null)
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        if (addressId == null || number == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        BigInteger userId = AuthorizedUser.id();
//        User user = userService.findOne(userId);
//        Address address = addressService.findOne(addressId);
//        Message message1 = new Message(LocalDateTime.now(), user, address, message.getMessage());
//        messageService.save(message1, addressId, userId);
//        List<Message> messageList = messageService.findByAddressAndIdGreaterThan(address, addressId);
//        return new ResponseEntity<>(messageList, HttpStatus.OK);
//    }

    @GetMapping("/me/chat/{addressId}/{messageId}")
    public ResponseEntity<List<Message>> findAllFromId(@PathVariable("addressId") BigInteger addressId, @PathVariable("messageId") BigInteger messageId) {
        BigInteger userId = AuthorizedUser.id();
        if (addressId == null || messageId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Message message = messageService.findOne(messageId);
        Address address = addressService.findOne(addressId);
        if (message == null || address == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<Message> messages = messageService.findByAddressAndIdGreaterThan(address, messageId);
        if (messages == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        messages.stream().filter(m -> m.getAuthor().getId().equals(userId)).forEach(m -> m.setSelf(true));
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping(value = "/me/chat/{addressId}")
    public ResponseEntity<List<Message>> findAllFromAddress(@PathVariable("addressId") BigInteger addressId) {
        if (addressId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        BigInteger userId = AuthorizedUser.id();
        List<Message> messages = messageService.findAllAddressMessages(addressId);
        if (messages == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        messages.stream().filter(message -> message.getAuthor().getId().equals(userId)).forEach(message -> message.setSelf(true));
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping(value = "/me/chat/{addressId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> createMessage(@RequestBody UserMessage userMessage, @PathVariable("addressId") BigInteger addressId) {
        if (addressId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Address address = addressService.findOne(addressId);
        BigInteger userId = AuthorizedUser.id();
        User user = userService.findOne(userId);
        Message message = new Message(LocalDateTime.now(), user, address, userMessage.getMessage());
        if (userMessage.equals("")) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        message = messageService.save(message, addressId, userId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
