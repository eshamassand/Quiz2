/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package QuizProject.SetupClient;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Esha
 */
public interface QuizSetupClientsInterface extends Serializable {

    /**
     * Allows the Client to add the question to the Quiz which persists on the QuizServer. 
     * @param question
     * @return array with the questions.
     */
    String[] clientAddsAnswers(String question);

    /**
     * Allows the client to add a series of Questions to a Quiz ID
     * @param id of Quiz
     * @return the questions that have been added in ArrayList
     * @throws RemoteException
     */
    ArrayList<String> clientAddsSetOfQuestions(int id) throws RemoteException;

    /**
     * Serializes the QuizServer.
     * @throws RemoteException
     */
    void closeDown() throws RemoteException;

    /**
     * Takes the user choice and acts accordingly (uses switch)
     * @param choice
     * @throws RemoteException
     */
    void dealWithSwitchRequest(int choice) throws RemoteException;

      /**
     * Returns the correct answers for the Player.
     * @param selectedQuizID to play
     * @throws RemoteException
     */
    void getAnswers(int selectedQuizID) throws RemoteException;
    
    
    /**
     * Until user terminates, presents the menu() after action is complete.
     * @throws RemoteException
     */
    void keepLooping() throws RemoteException;
   
    /**
     * Launches the Setup client from a non-static method.
     * @throws RemoteException
     */
    void launch() throws RemoteException;

    /**
     * Presents a menu to screen for possible actions. Returns the choice from the menu.
     * @return choice.
     */
    int menu();
    
}
