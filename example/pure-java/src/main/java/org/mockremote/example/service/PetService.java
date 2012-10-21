package org.mockremote.example.service;

import org.mockremote.example.model.Pet;

import java.util.List;

public interface PetService {

    List<Pet> listByName(String name);
}
