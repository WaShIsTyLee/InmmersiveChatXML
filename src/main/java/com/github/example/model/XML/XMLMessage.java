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
    public static File archivoMessages = new File("messages.xml");
    public static File archivoTxt = new File("messages.txt");
    public static File archivoResumenTXT = new File("resumen.txt");


    public static void addMessage(Message message) throws Exception {
        List<Message> messages = recoverMessages();
        messages.add(message);
        saveMessages(messages);
    }

    public static List<Message> recoverMessages() throws JAXBException {
        if (archivoMessages.exists() && archivoMessages.length() > 0) {
            JAXBContext context = JAXBContext.newInstance(MessageWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            MessageWrapper wrapper = (MessageWrapper) unmarshaller.unmarshal(archivoMessages);
            return wrapper.getMessages();
        } else {
            return new ArrayList<>();
        }
    }


    public static void saveMessages(List<Message> messages) throws Exception {
        MessageWrapper wrapper = new MessageWrapper();
        wrapper.setMessages(messages);

        JAXBContext context = JAXBContext.newInstance(MessageWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, archivoMessages);
    }



        // Archivo para la conversación
        public static void convertirXMLaTxt(File archivoTxt, List<Message> mensajes) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoTxt))) {
                if (mensajes.isEmpty()) {
                    bw.write("No hay mensajes entre estos usuarios.");
                    return;
                }

                // Obtener el nombre de los usuarios (emisor y receptor)
                String emisorNickname = mensajes.get(0).getContactoEmisor().getNickname();
                String receptorNickname = mensajes.get(0).getContactoReceptor().getNickname();

                bw.write("Conversación entre " + emisorNickname + " y " + receptorNickname + ":");
                bw.newLine();
                bw.write("------------------------------------------------");
                bw.newLine();

                // Variables para el análisis
                int totalMensajes = 0;
                int[] contadorMensajesPorUsuario = new int[mensajes.size()];
                String[] usuarios = new String[mensajes.size()];
                String[] palabras = new String[1000]; // Arreglo para palabras
                int[] contadorPalabras = new int[1000]; // Contador para cada palabra
                int indicePalabras = 0;

                // Recorrer todos los mensajes
                for (Message message : mensajes) {
                    totalMensajes++;
                    String emisor = message.getContactoEmisor().getNickname();
                    String texto = message.getText();

                    // Escribir el mensaje en el archivo .txt
                    bw.write("Fecha: " + message.getFecha());
                    bw.newLine();
                    bw.write(emisor + ": " + texto);
                    bw.newLine();
                    bw.write("-------------------------------");
                    bw.newLine();

                    // Contar mensajes por usuario
                    boolean encontrado = false;
                    for (int i = 0; i < totalMensajes; i++) {
                        if (usuarios[i] != null && usuarios[i].equals(emisor)) {
                            contadorMensajesPorUsuario[i]++;
                            encontrado = true;
                            break;
                        }
                    }
                    if (!encontrado) {
                        usuarios[totalMensajes - 1] = emisor;
                        contadorMensajesPorUsuario[totalMensajes - 1] = 1;
                    }

                    // Contar palabras en el mensaje
                    String[] palabrasMensaje = texto.split("\\s+");
                    for (String palabra : palabrasMensaje) {
                        palabra = palabra.toLowerCase().replaceAll("[^a-zA-Záéíóúñ]", ""); // Normalizar palabras
                        boolean palabraEncontrada = false;

                        // Verificar si la palabra ya está en el arreglo de palabras
                        for (int i = 0; i < indicePalabras; i++) {
                            if (palabras[i].equals(palabra)) {
                                contadorPalabras[i]++;
                                palabraEncontrada = true;
                                break;
                            }
                        }

                        // Si la palabra no fue encontrada, agregarla
                        if (!palabraEncontrada && !palabra.isEmpty()) {
                            palabras[indicePalabras] = palabra;
                            contadorPalabras[indicePalabras]++;
                            indicePalabras++;
                        }
                    }
                }

                // Obtener la palabra más usada
                String palabraMasUsada = obtenerPalabraMasUsada(palabras, contadorPalabras, indicePalabras);

                // Obtener el usuario que más mensajes ha enviado
                String usuarioMasMensajes = obtenerUsuarioMasMensajes(usuarios, contadorMensajesPorUsuario, totalMensajes);

                // Generar archivo resumen
                generarArchivoResumen(new File("resumen.txt"), palabraMasUsada, usuarioMasMensajes, totalMensajes);

                System.out.println("Los mensajes han sido convertidos a un archivo .txt en formato de conversación.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Método para obtener la palabra más usada
        public static String obtenerPalabraMasUsada(String[] palabras, int[] contadorPalabras, int indicePalabras) {
            String palabraMasUsada = null;
            int maxContador = 0;

            for (int i = 0; i < indicePalabras; i++) {
                if (contadorPalabras[i] > maxContador) {
                    palabraMasUsada = palabras[i];
                    maxContador = contadorPalabras[i];
                }
            }

            return palabraMasUsada != null ? palabraMasUsada : "N/A";
        }

        // Método para obtener el usuario que más mensajes ha enviado
        public static String obtenerUsuarioMasMensajes(String[] usuarios, int[] contadorMensajesPorUsuario, int totalMensajes) {
            String usuarioMasMensajes = null;
            int maxContador = 0;

            for (int i = 0; i < totalMensajes; i++) {
                if (usuarios[i] != null && contadorMensajesPorUsuario[i] > maxContador) {
                    usuarioMasMensajes = usuarios[i];
                    maxContador = contadorMensajesPorUsuario[i];
                }
            }

            return usuarioMasMensajes != null ? usuarioMasMensajes : "N/A";
        }

        // Método para generar el archivo resumen.txt
        public static void generarArchivoResumen(File archivoResumen, String palabraMasUsada, String usuarioMasMensajes, int totalMensajes) throws IOException {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoResumen))) {
                bw.write("Resumen de la Conversación");
                bw.newLine();
                bw.write("----------------------------");
                bw.newLine();
                bw.write("Total de mensajes: " + totalMensajes);
                bw.newLine();
                bw.write("Usuario con más mensajes: " + usuarioMasMensajes);
                bw.newLine();
                bw.write("Palabra más usada: " + palabraMasUsada);
                bw.newLine();
                bw.write("----------------------------");
            }
            System.out.println("El archivo resumen.txt ha sido generado correctamente.");
        }
    }





