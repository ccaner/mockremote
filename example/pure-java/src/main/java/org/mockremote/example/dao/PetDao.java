package org.mockremote.example.dao;

import org.mockremote.example.model.Pet;

import java.util.List;

public interface PetDao {

    List<Pet> loadByName(String name);

}
