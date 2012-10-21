package org.mockremote.example.dao;

import org.mockremote.example.model.Pet;

import javax.swing.*;
import java.util.Collections;
import java.util.List;


public class PetDaoImpl implements PetDao {

    public static PetDao INSTANCE = new PetDaoImpl(new Object());

    private PetDaoImpl(Object someObject) {
    }

    @Override
    public List<Pet> loadByName(String name) {
        Pet pet = new Pet();
        pet.setName("LoadedByService");
        return Collections.singletonList(pet);
    }


    public static void main(String[] args) {
        int oX = 0, oY = 0;
        int width = 10, height = 5;

        String coords = JOptionPane.showInputDialog(null, "Enter Coords:");
        int x = Integer.parseInt(coords.split(" ")[0]);
        int y = Integer.parseInt(coords.split(" ")[1]);

        boolean inside = Math.abs(oX - x) <= (width/2) && Math.abs(oY - y) <= (height/2);
        if (inside) {
            JOptionPane.showMessageDialog(null, "Point: " + x + ", " + y + " is in rectangle.");
        } else {
            JOptionPane.showMessageDialog(null, "Point: " + x + ", " + y + " is not in rectangle.");
        }

    }

}
