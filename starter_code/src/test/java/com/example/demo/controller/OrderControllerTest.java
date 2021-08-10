package com.example.demo.controller;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository  orderRepository;

    private Item testItem;
    private User testUser;
    private UserOrder testUserOrder;
    private Cart testCart;

    private OrderController orderController;

    @Before
    public void setup(){
        orderController=new OrderController(userRepository,orderRepository);

        testItem=getTestItem();
        testUser= getTestUser();

        List<Item> items=getAllItems();

        testCart=getTestCart(items,testUser);
        testUser.setCart(testCart);
        testUserOrder=getTestUserOrder(items,testUser);

        List<UserOrder> userOrders=new ArrayList<>();
        userOrders.add(testUserOrder);

        Mockito.when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(orderRepository.findByUser(testUser)).thenReturn(userOrders);
    }

    @Test
    public void submit(){
        ResponseEntity<UserOrder> response = orderController.submit(testUser.getUsername());
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
        UserOrder receivedOrder = response.getBody();
        Assert.assertNotNull(receivedOrder);
        Assert.assertEquals(receivedOrder.getUser().getUsername(),testUser.getUsername());
        Assert.assertEquals(receivedOrder.getTotal(),testUserOrder.getTotal());
    }

    @Test
    public void getOrdersByUser(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(testUser.getUsername());
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
        List<UserOrder> receivedOrders = response.getBody();
        Assert.assertNotNull(receivedOrders);
        Assert.assertEquals(testUserOrder,receivedOrders.get(0));
        Assert.assertEquals(1,receivedOrders.size());
    }

    private User getTestUser(){
        User user=new User();
        user.setId(1);
        user.setPassword("hashedPassword");
        user.setUsername("Bhaskar");
        return user;
    }

    private Item getTestItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Pen");
        item.setPrice(BigDecimal.valueOf(5.66));
        item.setDescription("Gel Pen");
        return item;
    }

    private List<Item> getAllItems(){
        testItem=getTestItem();
        List<Item> items=new ArrayList<>();
        items.add(testItem);
        return items;
    }

    private Cart getTestCart(List<Item> items,User user){
        Cart cart=new Cart();
        cart.setId(1L);
        cart.setItems(items);
        cart.setUser(user);
        return cart;
    }

    private UserOrder getTestUserOrder(List<Item> items,User user){
        UserOrder order=new UserOrder();
        order.setId(1L);
        order.setUser(user);
        order.setItems(items);
        return order;
    }

    private ModifyCartRequest getTestCartRequest(){
        ModifyCartRequest cartRequest=new ModifyCartRequest();
        cartRequest.setItemId(1L);
        cartRequest.setUsername("Bhaskar");
        cartRequest.setQuantity(1);
        return cartRequest;
    }
}
