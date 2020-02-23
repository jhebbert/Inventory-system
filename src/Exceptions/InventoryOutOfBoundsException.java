/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author james
 */
public class InventoryOutOfBoundsException extends Exception {
    public static final long serialVersionUID = 42L;
    
    @Override
    public String getMessage() {
        return "Stock value out of bounds.";
    }
}
