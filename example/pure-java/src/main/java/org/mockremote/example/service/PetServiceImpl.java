package org.mockremote.example.service;

import org.mockremote.example.dao.PetDao;
import org.mockremote.example.dao.PetDaoImpl;
import org.mockremote.example.model.Pet;

import java.util.List;

public class PetServiceImpl implements PetService {

    public static final PetServiceImpl INSTANCE = new PetServiceImpl();

    private PetDao petDao = PetDaoImpl.INSTANCE;

    private PetServiceImpl() {
    }

    @Override
    public List<Pet> listByName(String name) {
        return petDao.loadByName(name);
    }

}
