package com.iolma.tests;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class AudioTest extends JFrame {
    private JButton chat = new JButton("Voice");
    private GUIListener gl = new GUIListener();
    private IncomingSoundListener isl = new IncomingSoundListener();
    private OutgoingSoundListener osl = new OutgoingSoundListener();
    private boolean inVoice = true;
    private boolean outVoice = false;
    AudioFormat format = getAudioFormat();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

public AudioTest() throws IOException {
    //new Thread(tl).start();
    new Thread(isl).start();
    Container contentPane = this.getContentPane();
    this.setSize(200,100);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    chat.setBounds(10,10,80,30);
    chat.addActionListener(gl);
    contentPane.add(chat);
    this.setVisible(true);
}

private AudioFormat getAudioFormat() {
    float sampleRate = 48000.0F;
    int sampleSizeBits = 16;
    int channels = 1;
    boolean signed = true;
    boolean bigEndian = false;
    //AudioFormat.Encoding.ULAW
    return new AudioFormat(sampleRate, sampleSizeBits, channels, signed, bigEndian);
}

class GUIListener implements ActionListener {

    public void actionPerformed(ActionEvent actionevent) {
        String action = actionevent.getActionCommand();
        if ("Mute".equals(action)) {
            outVoice = false;
            chat.setText("Voice");
        }
        if ("Voice".equals(action)) {
            new Thread(osl).start(); 
            outVoice = true;
            chat.setText("Mute");
        }
    }
}

class IncomingSoundListener implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("Listening for incoming sound");
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
            speaker.open(format);
            speaker.start();
            while(inVoice) { 
                byte[] data = baos.toByteArray();
                baos.reset();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                AudioInputStream ais = new AudioInputStream(bais,format,data.length);
                int numBytesRead = 0;
                if ((numBytesRead = ais.read(data)) != -1) {
                    System.out.println(numBytesRead);
                	speaker.write(data, 0, numBytesRead);
                }
                ais.close();
                bais.close();
            }
            speaker.drain();
            speaker.close();
            System.out.println("Stopped listening for incoming sound");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class OutgoingSoundListener implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("Listening for outgoing sound");
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(micInfo);
            mic.open(format);
            byte tmpBuff[] = new byte[mic.getBufferSize()/5];
            mic.start();
            while(outVoice) {
                int count = mic.read(tmpBuff,0,tmpBuff.length);
                if (count > 0) baos.write(tmpBuff, 0, count);
            }
            mic.drain();
            mic.close();
            System.out.println("Stopped listening for outgoing sound");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

/**
 * @param args
 * @throws IOException 
 */
public static void main(String[] args) throws IOException {
    new AudioTest();    
}
}