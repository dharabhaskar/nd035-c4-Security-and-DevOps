package com.example.demo.controller;


import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {
    @Mock
    private ItemRepository itemRepositoryMocked;

    private ItemController itemController;

    private Item testItem;

    @Before
    public void setup() {
        itemController = new ItemController(itemRepositoryMocked);
        testItem = getItem();
        ArrayList<Item> allItems = new ArrayList<>();
        allItems.add(testItem);
        Mockito.when(itemRepositoryMocked.findAll()).thenReturn(allItems);
        Mockito.when(itemRepositoryMocked.findByName(testItem.getName())).thenReturn(allItems);
        Mockito.when(itemRepositoryMocked.findById(testItem.getId())).thenReturn(Optional.of(testItem));
        Mockito.when(itemRepositoryMocked.save(testItem)).thenReturn(testItem);
    }

    @Test
    public void testGetAllItems() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> receivedItemList = response.getBody();
        Assert.assertEquals(1, receivedItemList.size());
        Assert.assertEquals(this.testItem, receivedItemList.get(0));
    }

    @Test
    public void testFindById() {
        ResponseEntity<Item> response = itemController.getItemById(testItem.getId());
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Item receivedItem = response.getBody();
        Assert.assertEquals(testItem.getId(), receivedItem.getId());
    }

    @Test
    public void testFindByName() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName(testItem.getName());
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> items = response.getBody();
        Assert.assertEquals(testItem, items.get(0));
    }

    @Test
    public void testSaveItem(){
        ResponseEntity<Item> response = itemController.saveItem(testItem);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Item receivedItem = response.getBody();
        Assert.assertEquals(testItem.getId(), receivedItem.getId());
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
