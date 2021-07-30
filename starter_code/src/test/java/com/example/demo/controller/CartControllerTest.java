package com.example.demo.controller;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserController userController;

    private CreateUserRequest testUserReq;
    private User testUser;
    private Item testItem;
    private ModifyCartRequest cartRequest;

    private CartController cartController;

    @Before
    public void setup(){
        testItem=getItem();
        testUserReq=getMockUser();
        userController=new UserController(userRepository,cartRepository,passwordEncoder);
        cartController=new CartController(userRepository,cartRepository,itemRepository);

        testUser=userController.createUser(testUserReq).getBody();
        Assert.assertNotNull(testUser);
        Mockito.when(userRepository.findByUsername(testUserReq.getUsername())).thenReturn(testUser);
        Mockito.when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));

        cartRequest=new ModifyCartRequest();
        cartRequest.setUsername(testUser.getUsername());
        cartRequest.setItemId(testItem.getId());
        cartRequest.setQuantity(2);
    }

    @Test
    public void testAddToCart(){
        ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());

        Cart cart=response.getBody();
        Assert.assertNotNull(cart);
        Assert.assertEquals(cartRequest.getQuantity(),cart.getItems().size());
    }

    @Test
    public void testRemoveFromCart(){
        //Adding  items to cart (2 items)...
        cartController.addTocart(cartRequest);

        //Removing (1 item)
        cartRequest.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());

        Cart cart=response.getBody();
        Assert.assertNotNull(cart);
        //Expecting 2-1 = 1 item on cart...
        Assert.assertEquals(1,cart.getItems().size());
    }


    private CreateUserRequest getMockUser(){
        CreateUserRequest cr=new CreateUserRequest();
        cr.setUsername("bhaskar");
        cr.setPassword("passCode");
        cr.setConfirmPassword("passCode");
        return cr;
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Pen");
        item.setPrice(BigDecimal.valueOf(5.66));
        item.setDescription("Gel Pen");
        return item;
    }
}
