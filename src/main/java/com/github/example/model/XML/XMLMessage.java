package com.github.example.model.XML;

import com.github.example.model.Entity.Message;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLMessage {
    public static File fileMessages = new File("messages.xml");
    public static File txtFile = new File("messages.txt");

    /**

     * Adds a new message to the list of messages.
     *
     * @param message The message to be added.
     * @throws Exception If there is an error during message recovery or saving.
     */
    public static void addMessage(Message message) throws Exception {
        List<Message> messages = recoverMessages();
        messages.add(message);
        saveMessages(messages);
    }


    /**
     * Recovers a list of messages from an XML file.
     *
     * @return A list of messages. If the file does not exist or is empty, an empty list is returned.
     * @throws JAXBException If there is an error during XML unmarshalling.
     */
    public static List<Message> recoverMessages() throws JAXBException {
        if (fileMessages.exists() && fileMessages.length() > 0) {
            JAXBContext context = JAXBContext.newInstance(MessageWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            MessageWrapper wrapper = (MessageWrapper) unmarshaller.unmarshal(fileMessages);
            return wrapper.getMessages();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Saves a list of messages to an XML file.
     *
     * @param messages The list of messages to be saved.
     * @throws Exception If there is an error during XML marshalling.
     */
    public static void saveMessages(List<Message> messages) throws Exception {
        MessageWrapper wrapper = new MessageWrapper();
        wrapper.setMessages(messages);
        JAXBContext context = JAXBContext.newInstance(MessageWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, fileMessages);
    }

    /**
     * Converts a list of messages to a text file in conversation format.
     *
     * @param txtFile The text file where the messages will be saved.
     * @param messages   The list of messages to be converted.
     */
    public static void convertXMLToTXT(File txtFile, List<Message> messages) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(txtFile))) {
            if (messages.isEmpty()) {
                bw.write("No hay mensajes entre estos usuarios.");
                return;
            }
            String senderNickname = messages.get(0).getSenderContact().getNickname();
            String receiverNickname = messages.get(0).getReceiverContact().getNickname();
            bw.write("Conversación entre " + senderNickname + " y " + receiverNickname + ":");
            bw.newLine();
            bw.write("------------------------------------------------");
            bw.newLine();
            int totalMessages = 0;
            int[] messageCountByUser = new int[messages.size()];
            String[] users = new String[messages.size()];
            String[] words = new String[1000];
            int[] CountWords = new int[1000];
            int wordIndex = 0;
            for (Message message : messages) {
                totalMessages++;
                String sender = message.getSenderContact().getNickname();
                String text = message.getText();
                bw.write("Fecha: " + message.getDate());
                bw.newLine();
                bw.write(sender + ": " + text);
                bw.newLine();
                bw.write("-------------------------------");
                bw.newLine();
                boolean found = false;
                for (int i = 0; i < totalMessages; i++) {
                    if (users[i] != null && users[i].equals(sender)) {
                        messageCountByUser[i]++;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    users[totalMessages - 1] = sender;
                    messageCountByUser[totalMessages - 1] = 1;
                }
                String[] wordMessage = text.split("\\s+");
                for (String word : wordMessage) {
                    word = word.toLowerCase().replaceAll("[^a-zA-Záéíóúñ]", "");
                    boolean wordIsFound = false;
                    for (int i = 0; i < wordIndex; i++) {
                        if (words[i].equals(word)) {
                            CountWords[i]++;
                            wordIsFound = true;
                            break;
                        }
                    }
                    if (!wordIsFound && !word.isEmpty()) {
                        words[wordIndex] = word;
                        CountWords[wordIndex]++;
                        wordIndex++;
                    }
                }
            }
            String moreUsedWord = getMoreUsedWord(words, CountWords, wordIndex);
            String userMoreMessages = getUserMoreMessages(users, messageCountByUser, totalMessages);
            generateSummaryFile(new File("resumen.txt"), moreUsedWord, userMoreMessages, totalMessages);
            System.out.println("Los mensajes han sido convertidos a un archivo .txt en formato de conversación.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtains the most frequently used word from a list of words and their counts.
     *
     * @param words An array of unique words.
     * @param WordsCounter An array containing the count of occurrences for each word.
     * @param WordsIndex The number of unique words in the array.
     * @return The most used word, or "N/A" if no words are found.
     */
    public static String getMoreUsedWord(String[] words, int[] WordsCounter, int WordsIndex) {
        String moreUsedWord = null;
        int maxCounter = 0;
        for (int i = 0; i < WordsIndex; i++) {
            if (WordsCounter[i] > maxCounter) {
                moreUsedWord = words[i];
                maxCounter = WordsCounter[i];
            }
        }
        return moreUsedWord != null ? moreUsedWord : "N/A";
    }

    /**
     * Obtains the user who sent the most messages from a list of users and their message counts.
     *
     * @param users An array of unique users.
     * @param CounterMessagesByUser An array containing the count of messages sent by each user.
     * @param totalMessages The total number of messages processed.
     * @return The user with the most messages, or "N/A" if no users are found.
     */
    public static String getUserMoreMessages(String[] users, int[] CounterMessagesByUser, int totalMessages) {
        String userMoreMessages = null;
        int maxContador = 0;

        for (int i = 0; i < totalMessages; i++) {
            if (users[i] != null && CounterMessagesByUser[i] > maxContador) {
                userMoreMessages = users[i];
                maxContador = CounterMessagesByUser[i];
            }
        }
        return userMoreMessages != null ? userMoreMessages : "N/A";
    }

    /**
     * Generates a summary file with details about the conversation.
     * @param summaryFile       The file to which the summary will be written.
     * @param moreUsedWord      The most used word in the conversation.
     * @param userMoreMessages   The user who sent the most messages.
     * @param totalMessages        The total number of messages in the conversation.
     * @throws IOException         If an I/O error occurs while writing to the file.
     */
    public static void generateSummaryFile(File summaryFile, String moreUsedWord, String userMoreMessages, int totalMessages) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(summaryFile))) {
            bw.write("Resumen de la Conversación");
            bw.newLine();
            bw.write("----------------------------");
            bw.newLine();
            bw.write("Total de mensajes: " + totalMessages);
            bw.newLine();
            bw.write("Usuario con más mensajes: " + userMoreMessages);
            bw.newLine();
            bw.write("Palabra más usada: " + moreUsedWord);
            bw.newLine();
            bw.write("----------------------------");
        }
        System.out.println("El archivo resumen.txt ha sido generado correctamente.");
    }
}





