/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
