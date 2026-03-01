package com.shopingweb.product_service.repository;

import com.shopingweb.product_service.model.Product;
import com.shopingweb.product_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository  extends MongoRepository<User,String> {

}

