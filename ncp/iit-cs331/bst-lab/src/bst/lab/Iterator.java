/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bst.lab;

/**
 *
 * @author Alvin
 */
interface Iterator<E>
{
    E get();
    void next();
    boolean isValid();
    void delete();
}
