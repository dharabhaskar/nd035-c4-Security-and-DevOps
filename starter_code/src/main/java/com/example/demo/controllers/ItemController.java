package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepositoryMocked) {
    	this.itemRepository=itemRepositoryMocked;
    }

	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @GetMapping
	public ResponseEntity<List<Item>> getItems() {
		log.info("ALL ITEMS RETRIEVED SUCCESSFULLY.");
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		log.info("ITEM BY ID FOUND");
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		log.info("ITEM BY NAME FOUND");
		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
	}

	@PostMapping()
	public ResponseEntity<Item> saveItem(@RequestBody Item item){
    	Item savedItem=itemRepository.save(item);
    	log.info("ITEM SAVED SUCCESSFULLY");
    	return ResponseEntity.ok(savedItem);
	}
	
}
