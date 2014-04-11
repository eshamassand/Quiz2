/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package QuizProject.Servers;

/**
 * StreamLines the process of Scanner in (and parsing to integer for int inputs).
 * @author Esha
 */
public interface GetInputInterf  {

    /**
     * Parses a String from keyboard to an int input.
     * @return int
     */
    int getIntInput();

    /**
     *
     * @return String representation of keyboard input.
     */
    String getStringInput();
      
}
