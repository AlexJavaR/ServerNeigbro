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
            ObjectId objectAddressId = new ObjectId(addressId.toString(16));
            ObjectId objectUserId = new ObjectId(userId.toString(16));
            Integer messages = messageRepository.IsMoreThen(objectAddressId, objectUserId);
            if (messages <= 100) {
                return messageRepository.save(message);
            } else {
                messageRepository.delete(messageRepository.findOneForDeleting(objectAddressId));
                return messageRepository.save(message);
            }

        } else return null;
    }

//    public List<Message> findAllMessagesOnAdressFromPostId(Address address, BigInteger messagepostid) {
//
//        ObjectId messId = new ObjectId(messagepostid.toString(16));
//        List<Message> temp = messageRepository.findByAddressAndIdGreaterThan(address, messId);
//        return temp;
//   }


//   public  List<Message> findByAddressOrderById( BigInteger addressId , BigInteger messageId)
//   {
//       Address address = addressRepository.findOne(addressId);
//       Message message = messageRepository.findMessageById(messageId);
//       if(address == null || message == null)
//           return null;
//       // check is nessesary
//       ObjectId addressobjectId = new ObjectId(addressId.toString(16));
//
//       ObjectId messageobjectId = new ObjectId(messageId.toString(16));
//
//
//// TODO compare to method ObjectID
//       List<Message> messages = messageRepository.findAllAddressMessages(addressobjectId);
//       if(messages == null) return null;
//       List<Message> postmessages = new LinkedList<>();
//       for(Message o : messages) {
//           ObjectId currentMessageobjectId = new ObjectId(o.getId().toString(16));
//           if(currentMessageobjectId.compareTo(messageobjectId)>0)
//           {
//                messages.add(messageRepository.findOne(messageRepository.findMessage(currentMessageobjectId).getId()));
//           }
//
//       }
//       return messages;
//   }


//    @Override
//    public List<Message> findAllAddressMessages(Address address) {
//        if(address!= null) {
//            ObjectId addressId = new ObjectId(address.getId().toString(16));
//            List<Message> messages = messageRepository.findAllAddressMessages(addressId);
//            return messages;
//        }
//        else return null;
//
//    }

    @Override
    public Message findOne(BigInteger messageId) {
        return messageRepository.findOne(messageId);
    }
}
