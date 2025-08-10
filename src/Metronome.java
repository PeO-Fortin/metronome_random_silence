import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledExecutorService;

public class Metronome {

    //Constantes de classe
    public static final int MILLI_PAR_MINUTE = 60000;

    //Attributs d'instances
    private int tempsBattement;
    private ScheduledExecutorService executor;
    private int toursSilence;
    private boolean enCours;

    private File sonBattement;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private DataLine.Info info;
    private Clip clipAudio;

    /**
     * Constructeur de la classe Metronome
     *
     */
    public Metronome() {
    }

    public void setTempsBattement(int bpm) {
        this.tempsBattement = calculTempsBattement(bpm);
    }

    public int getTempsBattement() {
        return tempsBattement;
    }

    public void setToursSilence(int toursSkip) {
        this.toursSilence = toursSkip;
    }

    public int getToursSilence() {
        return toursSilence;
    }

    public void lancer(){
        enCours = true;

        //Preparation du son du metronome
        try {
            sonBattement = new File("metronome.wav");
            audioStream = AudioSystem.getAudioInputStream(sonBattement);
            audioFormat = audioStream.getFormat();
            info = new DataLine.Info(Clip.class, audioFormat);
            clipAudio = (Clip) AudioSystem.getLine(info);
            clipAudio.open(audioStream);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Fichier audio de format incorrect.");
        } catch (IOException e) {
            System.out.println("Fichier audio introuvable.");
        } catch (LineUnavailableException e) {
            System.out.println("Fichier introuvable.");
        }

        while(enCours && clipAudio != null){
            try {
                clipAudio.setFramePosition(0); // Rembobiner au début
                clipAudio.start();
                Thread.sleep(tempsBattement);
                clipAudio.stop();
            } catch (InterruptedException e) {
                System.out.println("Interruption inattendue");
            }
        }
        nettoyerRessources();
    }

    /**
     * Methode pour arreter le metronome.
     */
    public void stop(){
        enCours = false;
    }

    /**
     * Calcul le nombre de millisecondes d'attente entre chaque battement pour
     * une minute.
     *
     * @param bpm Nombre de battements par minute souhaitee
     * @return Le nombre de millisecondes entre chaque battement
     */
    private int calculTempsBattement (int bpm) {
        return MILLI_PAR_MINUTE / bpm;
    }

    private void nettoyerRessources(){
        if (clipAudio != null) {
            if (clipAudio.isRunning()) {
                clipAudio.stop();
            }
            if (clipAudio.isOpen()) {
                clipAudio.close();
            }
            clipAudio = null;
        }

        if (audioStream != null) {
            try {
                audioStream.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture du flux audio.");
            }
            audioStream = null;
        }
    }
}
