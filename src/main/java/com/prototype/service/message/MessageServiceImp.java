package com.prototype.service.message;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.User;
import com.prototype.model.Message;
import com.prototype.repository.address.AddressRepository;
import com.prototype.repository.message.MessageRepository;
import com.prototype.repository.user.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;


@Service("MessageService")
public class MessageServiceImp implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public MessageServiceImp() {
    }

    //Save message//!!!//
    @Override
    public Message save(Message message, BigInteger addressId, BigInteger userId) {
        if (message == null || addressId == null || userId == null)
            return null;

        User user = userRepository.findOne(userId);
        Address address = addressRepository.findOne(addressId);
        Boolean valid = false;

        List<AddressData> addressData = user.getAddressDataList();
        if (addressData == null)
            return null;
        // check only the address. Add checking of the AddressData
        for (AddressData o : addressData) {
            if (valid)
                break;
            if (o.getAddress().equals(address))
                valid = true;
        }

        if (valid) {
            ObjectId objectAddressId;
            ObjectId objectUserId;
            try {
                objectAddressId = new ObjectId(addressId.toString(16));
                objectUserId = new ObjectId(userId.toString(16));
            } catch (IllegalArgumentException e) {
                return null;
            }
            Integer messages = messageRepository.IsMoreThen(objectAddressId, objectUserId);
            if (messages <= 100) {
                return messageRepository.save(message);
            } else {
                messageRepository.delete(messageRepository.findOneForDeleting(objectAddressId));
                return messageRepository.save(message);
            }

        } else return null;
    }

    @Override
    public List<Message> findByAddressAndIdGreaterThan(Address address, BigInteger messageId) {
        ObjectId objectMessageId = convertBigIntegerToObjectId(messageId);
        if (objectMessageId == null) {
            return null;
        }
        return messageRepository.findByAddressAndIdGreaterThan(address, objectMessageId);
    }

    @Override
    public List<Message> findAllAddressMessages(BigInteger addressId) {
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (objectAddressId == null) {
            return null;
        }
        if (addressRepository.findOne(addressId) != null) {
            List<Message> messages = messageRepository.findAllAddressMessages(objectAddressId);
            return messages;
        } else return null;
    }

    @Override
    public Message findOne(BigInteger messageId) {
        return messageRepository.findOne(messageId);
    }

    public ObjectId convertBigIntegerToObjectId(BigInteger addressId) {
        ObjectId objectAddressId;
        try {
            objectAddressId = new ObjectId(addressId.toString(16));
        } catch (IllegalArgumentException e) {
            return null;
        }
        return objectAddressId;
    }
}
