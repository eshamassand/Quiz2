/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuizProject.Servers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author Esha
 */
public class QuizServer extends UnicastRemoteObject implements QuizServerInterf {

    private Set<Quiz> quizzes = new HashSet<>(); //set of all current quizzes

    private Map<Integer, ArrayList<String>> quizMap = new HashMap<>();//map Quiz ID to List of Questions

    private Map<String, String[]> questionAnswers = new HashMap<>(); //holds an array, where pos[0] is the Question and pos[1-4] are the answers, pos[5] is the correct answer.

    private Map<Integer, Player> highestScorePlayerIDMap = new HashMap<>();// maps Quiz ID the highest scoring Player (holds player name, quiz ID and score for Quizzes)

    private String fileName = "quizData.txt";

    private int quizIDValue;

    public QuizServer() throws RemoteException {

        Set<Quiz> newQuizzes = null;
        Map<Integer, ArrayList<String>> newQuizMap = null;
        Map<String, String[]> newQuestionAnswers = null;
        Map<Integer, Player> newHighestScorePlayerIDMap = null;
        String newFileName = "quizData.txt";
        int newQuizIDValue;

        ObjectInputStream ois = null;

        if (new File(fileName).exists()) {

            System.out.println("FOUND QUIZDATA.TXT FILE.");
            try {
                ois = new ObjectInputStream(
                        new BufferedInputStream(
                                new FileInputStream(fileName)));
                {

                    newQuizzes = (Set<Quiz>) ois.readObject();
                    newQuizMap = (Map<Integer, ArrayList<String>>) ois.readObject();
                    newQuestionAnswers = (Map<String, String[]>) ois.readObject();
                    newHighestScorePlayerIDMap = (Map<Integer, Player>) ois.readObject();
                    newFileName = (String) ois.readObject();
                    newQuizIDValue = (int) ois.readObject();

                    System.out.println("ATTEMPTING TO RE-CREATE THE LAST STATE OF THE QUIZ SERVER.");
                    try {
                        Thread.sleep(500);
                        System.out.print(".");
                        Thread.sleep(500);
                        System.out.print(".");
                        Thread.sleep(500);
                        System.out.print(".");
                        Thread.sleep(500);
                        System.out.print(".");
                        Thread.sleep(500);
                        System.out.print(".");
                    } catch (InterruptedException ex) {
                        ex.getCause();
                    }

                    this.quizzes = newQuizzes;
                    this.quizMap = newQuizMap;
                    this.questionAnswers = newQuestionAnswers;
                    this.highestScorePlayerIDMap = newHighestScorePlayerIDMap;
                    this.fileName = newFileName;
                    this.quizIDValue = newQuizIDValue;

                    QuizID setIncrementingValue = new QuizID();
                    setIncrementingValue.setQuizIDNumber(newQuizIDValue);

                    System.out.println("SUCCESSFULLY COMPLETED DESERIALIZING THE QUIZSERVER.");

                }

            } catch (IOException | ClassNotFoundException ex) {
                System.err.println("ON WRITE ERROR " + ex);
            } finally {
                try {
                    ois.close();
                } catch (IOException ex) {
                    ex.getCause();
                    System.out.println("I/O EXCEPTION.");
                }
            }

        } else {
            System.out.print("LOOKING FOR PREVIOUS QUIZ DATA INFORMATION. \n"
                    + "PLEASE WAIT.");
            try {
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
            } catch (InterruptedException ex) {
                ex.getCause();
            }
            System.out.println("FILE NOT FOUND. CREATING QUIZDATA.TXT FILE");
        }
    }

    @Override
    public synchronized void serialize(Set<Quiz> quizzes,
            Map<Integer, ArrayList<String>> quizMap,
            Map<String, String[]> questionAnswers,
            Map<Integer, Player> highestScorePlayerIDMap,
            String fileName,
            int quizIDValue
    ) throws RemoteException, FileNotFoundException, IOException {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName)));

            oos.writeObject(quizzes);
            oos.writeObject(quizMap);
            oos.writeObject(questionAnswers);
            oos.writeObject(highestScorePlayerIDMap);
            oos.writeObject(fileName);
            oos.writeObject(quizIDValue);

            oos.close();

        } catch (FileNotFoundException ex) {
            ex.getCause();
        } catch (IOException ex) {
            ex.getCause();
        }
    }

    @Override
    public synchronized String getWinnerForQuiz(int quizID) throws RemoteException {
        String result = null;

        try {
            if (highestScorePlayerIDMap.containsKey(quizID)) {
                Player winner = highestScorePlayerIDMap.get(quizID);
                result = "THE WINNER FOR QUIZ " + quizID + " IS ";

                try {
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(500);
                System.out.print(".");
            } catch (InterruptedException ex) {
                ex.getCause();
            }
                System.out.println(winner.getPlayerName().toUpperCase() + "!!!" + "\n nHIGHEST SCORE: " + winner.getPlayerScore());
                
                
            } else {
                result = "NO SAVED HIGH SCORERS YET FOR THAT ID.";
            }
        } catch (NullPointerException ex) {
            result = "NO SAVED HIGH SCORERS YET FOR THAT ID.";
            System.out.println("NO SAVED HIGH SCORERS YET FOR THAT ID.");
            ex.getCause();
        }
        return result;
    }

    @Override
    public Map<Integer, Player> getHighestScorePlayerIDMap() throws RemoteException {
        return this.highestScorePlayerIDMap;
    }

    @Override
    public void setHighestScorePlayerIDMap(int id, Player player) throws RemoteException {
        this.highestScorePlayerIDMap.put(id, player);
    }

    @Override
    public Set<Quiz> getQuizzes() throws RemoteException {
        return this.quizzes;
    }

    @Override
    public Map<Integer, ArrayList<String>> getQuizMap() throws RemoteException {
        return this.quizMap;
    }

    @Override
    public Map<String, String[]> getQuestionsAndAnswers() throws RemoteException {
        return this.questionAnswers;
    }

    @Override
    public String getFileName() throws RemoteException {
        return this.fileName;
    }

    @Override
    public int getQuizIDValue() throws RemoteException {
        return this.quizIDValue;
    }

    @Override
    public void setQuizIDValue(int id) throws RemoteException {
        this.quizIDValue = id;
    }

    @Override
    public int getID() throws RemoteException {
        QuizID num = new QuizID();
        int generatedID = num.getQuizIDNumber();
        quizIDValue = generatedID;
        return generatedID;
    }

    @Override
    public synchronized int getHighestScoreForQuiz(int quizID) throws RemoteException, NullPointerException {
        int highestScoreForQuiz = 0;
        if (quizzes.isEmpty()) {
            throw new NullPointerException("NO QUIZZES EXIST. ");
        } else {
            for (Quiz a : quizzes) {
                if (a.getQuizID() == quizID) {
                    highestScoreForQuiz = a.getHighestScore();
                }
            }
        }
        return highestScoreForQuiz;
    }

    @Override
    public synchronized void setHighestScoreForQuiz(int QuizID, int score) throws RemoteException, NullPointerException {
        if (quizzes.isEmpty()) {
            throw new NullPointerException("ATTEMPTED TO SET HIGHEST SCORE FOR A QUIZ THAT DOES NOT EXIST. ");
        } else {
            for (Quiz a : quizzes) {
                if (a.getQuizID() == QuizID) {
                    if (a.getHighestScore() < score) {
                        a.setHighestScore(score);

                    }
                }
            }
        }
    }

    @Override
    public synchronized int addQuiz(String s) throws RemoteException {
        int id = getID();
        Quiz newQuiz = new Quiz();
        newQuiz.setQuizID(id);
        newQuiz.setQuizName(s);
        quizzes.add(newQuiz);
        quizMap.put(id, null);
        highestScorePlayerIDMap.put(id, null);
        System.out.println("CLIENT ADDED QUIZ: " + s);
        return id;
    }

    @Override
    public synchronized Object[] getCurrentQuizList() throws RemoteException, NullPointerException {

        System.out.println("CLIENT REQUESTED PRINTOUT OF QUIZZES:");
        Object[] quizArray;
        if ((quizzes.isEmpty()) | (quizzes == null)) {
            throw new NullPointerException("BUT THERE ARE NO SAVED QUIZZES. ");
        } else {
            quizArray = quizzes.toArray();

            for (Object a : quizArray) {
                Quiz b = (Quiz) a;
                
                System.out.println("ID: " + b.getQuizID() + "\t|| QUIZ NAME: " + b.getQuizName());
            }
        }
        return quizArray;
    }

    @Override
    public synchronized Object[] getListOfQuestionsInQuiz(int id) throws RemoteException {
        if (quizMap.containsKey(id)) {
            ArrayList<String> thisListOfQuestions = quizMap.get(id);
            Object[] thisArrayOfQuestions = thisListOfQuestions.toArray();
            return thisArrayOfQuestions;
        } else {
            Object[] message = new String[1];
            message[0] = "CLIENT TRIED TO ACCESS ID THAT DOES NOT EXIST";
            return message;
        }
    }

    @Override
    public String checkIfQuizIDExists(int ID) throws RemoteException, NullPointerException {
        String result = null;
        try {
            if (quizMap.containsKey(ID)) {
                result = "ADDING TO QUIZ: " + ID;
            } else {
                result = "CREATING QUIZ: " + ID;
            }
        } catch (NullPointerException e) {
            e.getCause();
        }
        return result;
    }

    @Override
    public synchronized void serverAddsSetOfQuestions(int ID, ArrayList<String> newListOfQuestions) throws RemoteException, IllegalArgumentException {
        try {
            if (quizMap.containsKey(ID)) {
                quizMap.put(ID, newListOfQuestions);
                System.out.println(quizMap.get(ID).toString());

                System.out.println("CLIENT ADDED QUESTION TO QUIZ:" + ID);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("CLIENT TRIED TO ACCESS QUIZ. THERE IS NO SUCH QUIZ WITH THAT ID. ");
            e.getCause();
        }
    }

    @Override
    public synchronized void serverAddsAnswers(String question, String[] answers) throws RemoteException, NullPointerException, IllegalArgumentException {
        try {
            questionAnswers.put(question, answers);
            System.out.println("QUIZ: " + question + " HAS BEEN ADDED TO THE CLIENT'S QUESTION/ANSWERS MAP. ");
        } catch (NullPointerException | IllegalArgumentException e) {
            e.getCause();
        }
    }
}
