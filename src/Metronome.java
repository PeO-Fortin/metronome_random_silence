import java.util.concurrent.ScheduledExecutorService;

public class Metronome {

    //Constantes de classe
    public static final int MILLI_PAR_MINUTE = 60000;

    //Attributs d'instances
    int tempsBattement;
    private ScheduledExecutorService executor;
    private int toursSilence;

    /**
     * Constructeur de la classe Metronome
     *
     * @param bpm Battements par minute
     * @param toursSilence Nombre de battements silencieux enchaine
     */
    public Metronome(int bpm, int toursSilence) {
        this.tempsBattement = calculTempsBattement(bpm);
        this.toursSilence = toursSilence;
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
}
