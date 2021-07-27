package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ItemControllerTest {
    @Mock
    private ItemRepository itemRepository;

    @Autowired
    private ItemController itemController;

    @Before
    public void setup(){

    }
}
