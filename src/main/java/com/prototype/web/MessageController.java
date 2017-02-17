package com.prototype.web;

import com.prototype.model.Address;
import com.prototype.model.User;
import com.prototype.model.Message;
import com.prototype.repository.message.MessageRepository;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.address.AddressService;
import com.prototype.service.message.MessageServiceImp;
import com.prototype.service.user.UserService;
import com.prototype.to.UserMessage;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = MessageController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {
    static final String REST_URL = "/api/v1";

    @Autowired
    private MessageServiceImp messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;


    @PostMapping(value = "/me/chat/{addressId}/{number}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Message>> createMessage(@PathVariable BigInteger addressId, @PathVariable BigInteger number, @RequestBody UserMessage message) {
        if (message == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        ObjectId objectMessageId = new ObjectId(number.toString(16));
        BigInteger userId = AuthorizedUser.id();
        User user = userService.findOne(userId);
        Address address = addressService.findOne(addressId);
        Message message1 = new Message(LocalDateTime.now(), user, address, message.getMessage());
        messageService.save(message1, addressId, userId);
        List<Message> messageList = messageRepository.findByAddressAndIdGreaterThan(address, objectMessageId);
        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }

    @GetMapping("/me/chat/{addressId}/{number}")
    public ResponseEntity<List<Message>> findAllFromId(@PathVariable BigInteger addressId, @PathVariable BigInteger number) {
        BigInteger userId = AuthorizedUser.id();
        ObjectId objectMessageId = new ObjectId(number.toString(16));
        Message message = messageService.findOne(number);
        Address address = addressService.findOne(addressId);
        if (message == null || address == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else {
            List<Message> messages = messageRepository.findByAddressAndIdGreaterThan(address, objectMessageId);
            if (messages == null)
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            else if (messages.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
                messages.stream().filter(m -> m.getAuthor().getId().equals(userId)).forEach(m -> m.setSelf(true));
                return new ResponseEntity<>(messages, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/me/chat/{addressId}")
    public ResponseEntity<List<Message>> findAllFromAddress(@PathVariable BigInteger addressId) {
        BigInteger userId = AuthorizedUser.id();
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
        List<Message> messages = messageRepository.findAllAddressMessages(objectAddressId);
        if (messages == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            messages.stream().filter(message -> message.getAuthor().getId().equals(userId)).forEach(message -> message.setSelf(true));
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/me/chat/{addressId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> createMessage(@RequestBody UserMessage userMessage, @PathVariable BigInteger addressId) {
        Address address = addressService.findOne(addressId);
        BigInteger userId = AuthorizedUser.id();
        User user = userService.findOne(userId);
        Message message = new Message(LocalDateTime.now(), user, address, userMessage.getMessage());
        if (userMessage.equals(""))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else if (address == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else {
            message = messageService.save(message, addressId, userId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
