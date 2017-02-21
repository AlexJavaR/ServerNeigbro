package com.prototype.repository.message;
import com.prototype.model.Address;
import com.prototype.model.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;
import java.util.List;


@Repository
public interface MessageRepository extends MongoRepository<Message, BigInteger> {

    @Query(value = "{'address.$id' : ?0, 'author.$id' : ?1}")
    List<Message> findAllUserMessagesOfAddress(ObjectId addressId, ObjectId userId);

    @Query(value = "{'address.$id' : ?0, 'author.$id' : ?1}", count = true)
    Integer IsMoreThen(ObjectId addressId, ObjectId userId);

    @Query(value = "{'address.$id' : ?0}")
    List<Message> findAllAddressMessages(ObjectId addressId);

    @Query(value = "{'address.$id' : ?0}")
    Message findOneForDeleting(ObjectId addressId);

    @Query(value = "{'address.$id' : ?0}")
    List<Message> findByAddressId(ObjectId addressId);

    //List<Message> findByAddressAndIdGreaterThan(@Param("address.$id") Address address, @Param("Id") ObjectId Id);
    List<Message> findByAddressAndIdGreaterThan(Address address, ObjectId messageId);

}
//
//    //TODO check the query and select
//
//    List<Message> findByAddressOrderByIdAsc(@Param("address") Address address);
//    Message findMessageById(@Param("Id") BigInteger Id);
//    List<Message> findByIdGreaterThan(@Param("Id") ObjectId Id);


