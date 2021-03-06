/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuizProject.SetupClient;

import QuizProject.Servers.ClosedQuiz;
import QuizProject.Servers.GetInput;
import QuizProject.Servers.GetInputInterf;
import QuizProject.Servers.Quiz;
import QuizProject.Servers.QuizServerData;
import QuizProject.Servers.QuizServerInterf;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Esha
 */
public class QuizSetupClients implements QuizSetupClientsInterface, Serializable {

    private QuizServerInterf serverQuiz;
    boolean running = true;

    public Remote service;

    public QuizSetupClients() throws NotBoundException, MalformedURLException, RemoteException {
        serverQuiz = new QuizServerData();
        Remote service = this.service = Naming.lookup("//127.0.0.1:1099/quiz");
        if (System.getSecurityManager() == null) {
        System.setSecurityManager(new RMISecurityManager());
        }
        System.out.println("\t\t\t\tWELCOME TO THE QUIZ SETUP TOOL!");
    }

    @Override
    public void launch() throws RemoteException {

        try {
            serverQuiz = (QuizServerInterf) service;

            keepLooping();

        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        try {
            QuizSetupClientsInterface quizClient = new QuizSetupClients();
            quizClient.launch();
        } catch ( NotBoundException | MalformedURLException | RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int menu() {
        int switchValue = 0;

        try {
            System.out.println("\n===================================================================================================");
            System.out.println("ACTIVE AND NEW QUIZ SERVICES: ");
            System.out.println("-> PRESS 1 TO ADD A NEW QUIZ.");
            System.out.println("-> PRESS 2 FOR THE 'ACTIVE QUIZZES' LIST.");
            System.out.println("-> PRESS 3 TO LIST QUESTIONS OF A SPECIFIED QUIZ ID");
            System.out.println("-> PRESS 4 FOR A LIST OF 'ACTIVE QUIZ' QUESTIONS, INCLUDING THEIR CORRECT MULTIPLE CHOICE ANSWERS.");

            System.out.println("\nEXISTING QUIZZES: ");
            System.out.println("-> PRESS 5 TO REVEAL WINNER!!!");
            System.out.println("-> PRESS 6 TO CLOSE A QUIZ DOWN AND REMOVE IT FROM THE ACTIVE QUIZ LIST.");
            
            System.out.println("\nCLOSED QUIZZES: ");
            System.out.println("-> PRESS 7 FOR THE 'CLOSED QUIZZES' LIST.");
            
            System.out.println("\nSAVE AND CLOSE: ");
            System.out.println("-> PRESS 8 TO SAVE.");
            System.out.println("-> PRESS 9 TO CLOSE DOWN THE SYSTEM.");
            System.out.println("===================================================================================================");

            GetInput input = new GetInput();
            switchValue = input.getIntInput();

        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return switchValue;
    }

    @Override
    public void keepLooping() throws RemoteException {
        if (running) {
            dealWithSwitchRequest(menu());
            keepLooping();
        } else {

            try {
                serverQuiz.serialize(
                        serverQuiz.getQuizzes(),
                        serverQuiz.getQuizMap(),
                        serverQuiz.getQuestionsAndAnswers(),
                        serverQuiz.getHighestScorePlayerIDMap(),
                        serverQuiz.getFileName(),
                        serverQuiz.getQuizIDValue(),
                        serverQuiz.getClosedQuizList()
                );
            } catch (IOException ex) {
                System.out.println("COULD NOT LOCATE FILE.");
                ex.printStackTrace();
            }

            System.exit(0);
        }
    }

    @Override
    public synchronized ArrayList<String> clientAddsSetOfQuestions(int id) throws RemoteException, NullPointerException, IllegalArgumentException {
        ArrayList<String> newListOfQuestions = new ArrayList<String>();
        String question;
        String[] answers;

        boolean collectingQ = true;
        while (collectingQ) {
            System.out.println("ENTER A QUESTION: (END TO QUIT)");
            GetInputInterf input = new GetInput();
            question = input.getStringInput();

            if (question.equalsIgnoreCase("end")) {
                System.out.println("SAVED SUCCESSFULLY.");
                serverQuiz.serverAddsSetOfQuestions(id, newListOfQuestions);
                collectingQ = false;
                System.out.println("SETUP COMPLETE.");
                try {
                    serverQuiz.serialize(
                            serverQuiz.getQuizzes(),
                            serverQuiz.getQuizMap(),
                            serverQuiz.getQuestionsAndAnswers(),
                            serverQuiz.getHighestScorePlayerIDMap(),
                            serverQuiz.getFileName(),
                            serverQuiz.getQuizIDValue(),
                            serverQuiz.getClosedQuizList()
                    );
                } catch (IOException ex) {
                    System.out.println("COULD NOT LOCATE FILE.");
                    ex.printStackTrace();
                }
            } else {
                try {
                    newListOfQuestions.add(question);
                    answers = clientAddsAnswers(question);
                    serverQuiz.serverAddsAnswers(question, answers);
                    serverQuiz.getQuestionsAndAnswers().put(question, answers);
                } catch (NullPointerException | IllegalArgumentException | RemoteException e) {
                    System.out.println("INVALID INPUT. LET'S START THIS ONE AGAIN.");
                    e.printStackTrace();
                }

            }
        }

        Object[] list = newListOfQuestions.toArray();
        try {
            for (Object a : list) {
                System.out.println("ADDED: " + a.toString());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return newListOfQuestions;
    }

    @Override
    public synchronized String[] clientAddsAnswers(String question) {
        String[] answers = new String[6];
        System.out.println("ENTER YOUR MULTIPLE ANSWER CHOICES");

        GetInputInterf input = new GetInput();

        answers[0] = question;
        System.out.println("CHOICE 1:");

        String ans1 = input.getStringInput();
        answers[1] = ans1;

        System.out.println("CHOICE 2:");
        String ans2 = input.getStringInput();
        answers[2] = ans2;

        System.out.println("CHOICE 3:");
        String ans3 = input.getStringInput();
        answers[3] = ans3;

        System.out.println("CHOICE 4:");
        String ans4 = input.getStringInput();
        answers[4] = ans4;

        System.out.println("ENTER NUMBER OF CORRECT ANSWER (1,2,3 OR 4):");

        String ans5 = input.getStringInput();
        if ((ans5.equals("1")) | (ans5.equals("2")) | (ans5.equals("3")) | (ans5.equals("4"))) {
            answers[5] = ans5;
            System.out.println("QUESTION ADDED!");
        } else {
            throw new IllegalArgumentException("INVALID INPUT.");
        }
        return answers;

    }

    @Override
    public synchronized void getAnswers(int selectedQuizID) throws RemoteException {
        Map<Integer, ArrayList<String>> quizMap = null;
        ArrayList<String> questions = null;

        try {
            quizMap = serverQuiz.getQuizMap();
            questions = quizMap.get(selectedQuizID);
            for (String a : questions) {
                String[] temp = serverQuiz.getQuestionsAndAnswers().get(a);
                System.out.println("\nQUESTION: " + a + "\nTHE MULTIPLE CHOICE OPTIONS ARE: ");
                System.out.println("OPTION 1: " + temp[1]);
                System.out.println("OPTION 2: " + temp[2]);
                System.out.println("OPTION 3: " + temp[3]);
                System.out.println("OPTION 4: " + temp[4]);
                System.out.println("THE CORRECT MULTIPLE CHOICE OPTION TO SELECT IS: " + temp[5]);
            }
        } catch (NullPointerException e) {
            System.out.println("NO ANSWERS STORED YET! YOU COULD ADD SOME NOW!");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void dealWithSwitchRequest(int choice) throws RemoteException {
        switch (choice) {
            case 1: //deal with add a new Quiz
                try {
                    System.out.println("ENTER NEW QUIZ'S NAME:");
                    GetInputInterf input = new GetInput();
                    String name = input.getStringInput();
                    int id = serverQuiz.addQuiz(name);
                    System.out.println("QUIZ ID: \"" + id + "\"");
                    System.out.println("ENTER THE QUESTIONS. TYPE 'END' TO FINISH. ");
                    ArrayList<String> questionSet = clientAddsSetOfQuestions(id);
                    serverQuiz.serverAddsSetOfQuestions(id, questionSet);
                } catch (NullPointerException | RemoteException e) {
                    System.out.println("YOU DIDNT ENTER AN INPUT. LET'S DO THIS ONE AGAIN.");
                    e.getCause();
                }
                break;
            case 2:
                //get current quiz list
                try {
                    Object[] quizList = serverQuiz.getCurrentQuizList();
                    System.out.println("CURRENT QUIZ LIST: ");
                    for (Object a : quizList) {
                        Quiz b = (Quiz) a;
                        System.out.println("ID: " + b.getQuizID() + "\t|| QUIZ NAME: " + b.getQuizName());
                    }
                } catch (NullPointerException | RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    System.out.println("ENTER QUIZ ID:");
                    GetInputInterf input2 = new GetInput();
                    Object[] questions2 = serverQuiz.getListOfQuestionsInQuiz(input2.getIntInput());
                    for (Object a : questions2) {
                        System.out.println("QUESTION: " + a.toString());
                    }
                } catch (NullPointerException | RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case 4://QUOTE QUIZ ID AND REVEAL THE CURRENT SAVED ANSWERS FOR THE SETUP CLIENT.
                try {
                    System.out.println("ENTER QUIZ ID TO REVEAL CURRENT ANSWERS:");
                    GetInputInterf in = new GetInput();
                    int quizID = in.getIntInput();
                    getAnswers(quizID);
                } catch (NullPointerException | RemoteException e) {
                    e.printStackTrace();
                }
                break;
           
            case 5://QUOTE QUIZ ID AND CLOSE. FULL PLAYER DETAILS SAVED ON SERVER.
                try {
                    System.out.println("ENTER QUIZ ID TO REVEAL WINNER:");
                    GetInputInterf in = new GetInput();
                    int quizID = in.getIntInput();
                    System.out.println(serverQuiz.getWinnerForQuiz(quizID));
                } catch (NullPointerException | RemoteException e) {
                    e.printStackTrace();
                }
                break;
            
            case 6:
                try {
                    System.out.println("ENTER QUIZ ID TO CLOSE AND REMOVE FROM CURRENT QUIZ LIST: ");
                    GetInputInterf in = new GetInput();
                    int quizID = in.getIntInput();
                    serverQuiz.removeQuiz(quizID);
                    System.out.println("YOU CLOSED AND REMOVED QUIZ "+ quizID);
                    } catch (NullPointerException | RemoteException e) {
                        System.out.println("COULD NOT REMOVE THAT QUIZ. EITHER IT DIDN'T EXIST, OR IT HAD NO QUESTIONS STORED.");
                    e.printStackTrace();
                }
                    break;
            case 7:
                //get current quiz list
                try {
                    ArrayList<ClosedQuiz> listOfClosedQuizzes = serverQuiz.getClosedQuizList();
                    System.out.println("CLOSED QUIZ LIST: ");
                    for (ClosedQuiz a:listOfClosedQuizzes){
                        ClosedQuiz b = (ClosedQuiz) a;
                        System.out.println("QUIZ ID: " + b.getClosedQuizId()+ "\t|| HIGHEST PLAYER NAME: " + b.getPlayerName()+ "\t|| HIGHEST PLAYER SCORE: "+ b.getHighestScore());
                    }  
                    System.out.println("END OF LIST.");
                } catch (NullPointerException | RemoteException e) {
                    e.printStackTrace();
                }
                break;
             case 8: //exit given the Quiz ID
                System.out.print(".");
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
                } catch (InterruptedException ex) {
                    ex.getCause();
                }
                System.out.println("SAVED!");
                try {
                    serverQuiz.serialize(
                            serverQuiz.getQuizzes(),
                            serverQuiz.getQuizMap(),
                            serverQuiz.getQuestionsAndAnswers(),
                            serverQuiz.getHighestScorePlayerIDMap(),
                            serverQuiz.getFileName(),
                            serverQuiz.getQuizIDValue(),
                            serverQuiz.getClosedQuizList()
                    );
                } catch (IOException ex) {
                    System.out.println("COULD NOT LOCATE FILE.");
                    ex.printStackTrace();
                }
                break;
        
            case 9: //CLOSE DOWN
                System.out.println("CLOSING DOWN NOW....");
                System.out.println("(-: HAVE A GREAT DAY :-)");
                closeDown();
                break;
            default:
                System.out.println("SOMETHING WENT WRONG. PLEASE TRY AGAIN.");
                break;
        }
    }

    @Override
        public synchronized void closeDown() throws RemoteException {
            try {
                serverQuiz.serialize(
                        serverQuiz.getQuizzes(),
                        serverQuiz.getQuizMap(),
                        serverQuiz.getQuestionsAndAnswers(),
                        serverQuiz.getHighestScorePlayerIDMap(),
                        serverQuiz.getFileName(),
                        serverQuiz.getQuizIDValue(),
                        serverQuiz.getClosedQuizList()
                );
            } catch (IOException ex) {
                System.out.println("COULD NOT LOCATE FILE.");
                ex.printStackTrace();
            }
            System.exit(0);
        }

    }
