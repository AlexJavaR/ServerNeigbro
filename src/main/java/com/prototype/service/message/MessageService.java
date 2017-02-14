package com.prototype.service.message;

import com.prototype.model.Message;
import org.springframework.stereotype.Service;
import java.math.BigInteger;

@Service
public interface MessageService {
    Message save(Message message, BigInteger addressId, BigInteger userId);

    Message findOne(BigInteger messageId);
}
