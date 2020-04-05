package com.myapp.authenticateservice.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import com.myapp.authenticateservice.model.User;
import com.myapp.authenticateservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;

import javax.swing.text.html.Option;
import java.util.*;

@Service
public class CognitoUserPoolService {

    private UserRepository userRepository;

    @Autowired
    public CognitoUserPoolService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Value("${cognito.user.pool.id}")
    private String poolId;

    final AWSCognitoIdentityProvider provider =
            AWSCognitoIdentityProviderClientBuilder.defaultClient();

    public User addUser(){
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userRepository.findByUserId(auth.getName());
        AdminCreateUserRequest createUserRequest =new AdminCreateUserRequest();
        createUserRequest.setUserPoolId(poolId);
        createUserRequest.setTemporaryPassword(user.getPassword());
        createUserRequest.setMessageAction(MessageActionType.SUPPRESS);
        createUserRequest.setUsername(user.getUserName());
        createUserRequest.setUserAttributes(createUserAttributes(user));
        provider.adminCreateUser(createUserRequest);
        return user;
    }

    private List<AttributeType> createUserAttributes(User user) {
        List<AttributeType> attributes = new ArrayList<>();
        if(null!=user.getBirthDate()) {
            AttributeType dob = new AttributeType();
            dob.setName("birthdate");
            dob.setValue(user.getBirthDate());
            attributes.add(dob);
        }

        if(null!=user.getEmail()){
            AttributeType emailAttribute = new AttributeType();
            emailAttribute.setName("email");
            emailAttribute.setValue(user.getEmail());
            attributes.add(emailAttribute);
        }

        if(null!=user.getPhoneNumber()){
            AttributeType phoneAttribute = new AttributeType();
            phoneAttribute.setName("phone_number");
            phoneAttribute.setValue(user.getPhoneNumber());
            attributes.add(phoneAttribute);
        }
        return attributes;
    }

    public User getUser(final String userId) {
        User user= userRepository.findByUserId(userId);
        AdminGetUserRequest request = new AdminGetUserRequest();
        request.setUserPoolId(poolId);
        request.setUsername(user.getUserName());
        AdminGetUserResult result=provider.adminGetUser(request);
        user.setUserName(result.getUsername());
        return user;
    }
}
