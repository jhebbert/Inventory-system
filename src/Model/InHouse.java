/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Part;

/**
 *
 * @author james
 */
public class InHouse extends Part {
    
    private int machineID;

   
    public InHouse(int ID, String name, double price, int stock, int min, int max, int machineID) {
        super(ID, name, price, stock, min, max);
       
        this.machineID = machineID;
    }
 
    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
    
    
}
