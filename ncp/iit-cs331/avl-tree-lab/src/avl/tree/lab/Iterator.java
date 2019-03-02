/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package avl.tree.lab;

/**
 *
 * @author Alvin
 */
interface Iterator<E>
{
    E get();
    void next();
    boolean isValid();
}
