package com.prototype.service.message;

import com.prototype.model.Address;
import com.prototype.model.Message;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.List;

@Service
public interface MessageService {
    Message save(Message message, BigInteger addressId, BigInteger userId);

    Message findOne(BigInteger messageId);

    List<Message> findAllAddressMessages(BigInteger addressId);

    List<Message> findByAddressAndIdGreaterThan(Address address, BigInteger messageId);
}
