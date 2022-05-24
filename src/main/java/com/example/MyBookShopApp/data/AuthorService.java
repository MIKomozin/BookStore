package com.example.MyBookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authors = authorRepository.findAll();
        Map<String, List<Author>> unsortedMap = authors.stream().collect(Collectors.groupingBy((Author a) -> {
            return a.getName().substring(0, 1);
        }));
        return new TreeMap<>(unsortedMap);
    }
}
