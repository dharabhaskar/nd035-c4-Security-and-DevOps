package com.example.demo.controller;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserController userController;

    private CreateUserRequest testUserReq;
    private User testUser;

    private final String HASHED_PASSWORD="hashedPASSword";



    @Before
    public void setup(){
        testUserReq=getMockUser();
        userController=new UserController(userRepository,cartRepository,passwordEncoder);

        testUser=userController.createUser(testUserReq).getBody();
        Assert.assertNotNull(testUser);
        Mockito.when(userRepository.findByUsername(testUserReq.getUsername())).thenReturn(testUser);
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
    }

    @Test
    public void testCreateUser(){
        Mockito.when(passwordEncoder.encode(testUserReq.getPassword())).thenReturn(HASHED_PASSWORD);
        ResponseEntity<User> response = userController.createUser(testUserReq);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());

        User user=response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(testUserReq.getUsername(),user.getUsername());
        Assert.assertEquals(HASHED_PASSWORD,user.getPassword());
    }

    @Test
    public void testFindUserById(){
        ResponseEntity<User> response = userController.findById(testUser.getId());
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
        User user=response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(testUserReq.getUsername(),user.getUsername());
    }

    @Test
    public void testFindUserByName(){
        ResponseEntity<User> response = userController.findByUserName(testUser.getUsername());
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
        User user=response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(testUserReq.getUsername(),user.getUsername());
    }


    private CreateUserRequest getMockUser(){
        CreateUserRequest cr=new CreateUserRequest();
        cr.setUsername("bhaskar");
        cr.setPassword("passCode");
        cr.setConfirmPassword("passCode");
        return cr;
    }
}
