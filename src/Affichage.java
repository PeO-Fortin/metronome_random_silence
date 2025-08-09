import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Affichage implements ActionListener {
    //Constantes
    private final String MSG_ERR = "Vous devez entrer un nombre positif.";

    //Attributs d'instance
    private JFrame fenetre = new JFrame();
    private JLabel jLBpm = new JLabel("BPM");
    private JTextField jTFBpm = new JTextField(5);
    private JLabel jLSilence = new JLabel("Nombre de BPM silencieux");
    private JTextField jTFSilence = new JTextField(5);//8 colonnes
    private JCheckBox jCBRandom = new JCheckBox("Au hasard");
    private JButton jBDemarrer = new JButton("Démarrer");


    public Affichage () {
        init();
    }

    public void init() {
        fenetre.setLayout(new FlowLayout());
        fenetre.setBounds(400, 400, 700, 80);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setTitle("Metronome");
        jTFBpm.setForeground(Color.BLACK);
        jCBRandom.setSelected(false);
        fenetre.getContentPane().add(jLBpm);
        fenetre.getContentPane().add(jTFBpm);
        fenetre.getContentPane().add(jLSilence);
        fenetre.getContentPane().add(jTFSilence);
        fenetre.getContentPane().add(jCBRandom);
        fenetre.getContentPane().add(jBDemarrer);
        fenetre.setVisible(true);
//ajout des ecouteurs
        jCBRandom.addActionListener(this);
        jBDemarrer.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {

    }
}
