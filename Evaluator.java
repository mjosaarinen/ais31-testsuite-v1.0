import java.io.*;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.text.DecimalFormat;
import java.lang.*;



public class Evaluator extends JFrame implements ActionListener,ItemListener{
    Tester tester;
    final int elementzahl = 256*65536;
    static boolean geschwaetzig = true;
    static boolean byteformat = true;
    static byte testart = 1;
    static boolean normalertest = true;
    static int fortschrittzahl = 0;
    static int bitbreite = 0;
    static int bitzahl = 0;
    public static int c = 0;
    public static int letzterwert = 0;
    boolean zuwenigdaten = false;
    boolean abbruch = false;
    boolean skip = false;
    boolean dateigewaehlt = false;
    int letzteselement = -1;
    FileInputStream datei;
    DataInputStream bitdatei;
    String progname = new String("AIS 31 (V1) Referenzimplementierung - v1.0 ");
    String dateiname = new String("");
    File logfile;
    FileWriter logwriter;
    BufferedWriter logbuffer;
    PrintWriter log;
    private JRadioButton[] rb = new JRadioButton[9];
    private JLabel configlabel1 = new JLabel("Voreinstellungen:");
    private JLabel vorgangslabel4 = new JLabel("");
    private JLabel configtest = new JLabel("TRNG-Klasse:");
    private JLabel configausgabe = new JLabel("Ausgabe:");
    private JLabel configformat = new JLabel("Datenformat:");
    private JLabel vorgangsname = new JLabel("Fortschritt:");
    private JLabel configtestart = new JLabel("Testart:");
    private JLabel zahlenbreite = new JLabel("Interne Zufallszahlen:");
    public JTextField dateieingabe = new JTextField("In dieses Feld Dateinamen im Stil \"C:\\Testdaten\\Datei.dat\" eingeben",40); 
    public JTextField bitbreiteeingabe = new JTextField("Breite in Bit",7);
    private JProgressBar fortschritt = new JProgressBar(0,300);
    private JButton dateisuchen = new JButton("Datei suchen");
    private JButton hilfe = new JButton("Hilfe anzeigen");
    private JButton start5 = new JButton("S  T  A  R  T");
    private JScrollPane textpanel7;
    private JButton vorgangabbruch = new JButton("Abbrechen");
    private JTextArea textbox = new JTextArea(5,50);
    boolean logfileenabled = true;
    
    
    
    public Evaluator(){
        super("AIS 31 (V1) Referenzimplementierung der stat. Tests - v1.0 [Kein Job]");
        setBounds(100,100,700,550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try{
            logfile = new File("AIS 31.log");
            logwriter = new FileWriter(logfile);
            logbuffer = new BufferedWriter(logwriter);
            log = new PrintWriter(logbuffer);
        } catch (IOException e){
            logfileenabled = false;
            kommentar("Erstellen von AIS 31.log fehlgeschlagen: " + e.toString(),true,0);
        }
        JPanel hauptpanel = new JPanel();
        JPanel dateipanel2 = new JPanel();
        JPanel configpanel3 = new JPanel();
        JPanel vorgangspanel6 = new JPanel();
        rb[0] = new JRadioButton("P1: T1 - T5", true);
        rb[0].setToolTipText("TNRG-Klasse P1, Test T1 - T5 wählen");
        rb[1] = new JRadioButton("P2 - spezifisch", false);
        rb[1].setToolTipText("TNRG-Klasse P2 (spezifische Tests) wählen");
        rb[2] = new JRadioButton("Normal", false);
        rb[2].setToolTipText("Ausgabe ohne Details (Testdaten, Debug-Infos, etc ...) wählen");
        rb[3] = new JRadioButton("Detailliert", true);
        rb[3].setToolTipText("Ausgabe mit Details (Testdaten, Debug-Infos, etc ...) wählen");
        rb[4] = new JRadioButton("1Byte = 1RNDBit", true);
        rb[4].setToolTipText("In 1 Dateibyte befindet sich genau 1 Zufallsbit");
        rb[5] = new JRadioButton("1Byte = 8RNDBit", false);
        rb[5].setToolTipText("In 1 Dateibyte befinden sich genau 8 Zufallsbits, älteres ist höchstwertiges");
        rb[6] = new JRadioButton("Normaler Test", true);
        rb[6].setToolTipText("TNRG unterzieht sich normalem Test, keinem Wiederholungstest");
        rb[7] = new JRadioButton("Wiederholung", false);
        rb[7].setToolTipText("TNRG wird Wiederholungstest unterzogen, da 1. Test gescheitert");
        rb[8] = new JRadioButton("P1: T0", false);
        rb[8].setToolTipText("TNRG-Klasse P1, Test T0 wählen");
        fortschritt.setStringPainted(true);
        vorgangabbruch.setBackground(Color.red);
        vorgangabbruch.setToolTipText("Während der zeitaufwändigen Testsuite (P0/T1-T5) kann hiermit der Test abgebrochen werden");
        start5.setToolTipText("Nach Dateneingabe hier klicken, um Test zu starten");
        dateisuchen.addActionListener(this);
        dateisuchen.setEnabled(false);
        dateisuchen.setToolTipText("Den Windows-Dateisuchen-Dialog öffnen (NOCH NICHT VOLLSTÄNDIG IMPLEMENTIERT, bitte Dateinamen von Hand eingeben)");
        start5.setBackground(Color.green);
        start5.addActionListener(this);
        vorgangabbruch.addActionListener(this);
        hilfe.addActionListener(this);
        hilfe.setToolTipText("Hilfetext/Anleitung im Logfenster abbilden");
        hilfe.setBackground(Color.yellow);
        for (int i = 0; i < rb.length; i++){
            rb[i].addItemListener(this);
        }
        GridBagLayout gblhaupt = new GridBagLayout();
        GridBagLayout gbl2 = new GridBagLayout();
        GridBagLayout gbl6 = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        dateipanel2.setLayout(gbl2);
        configpanel3.setLayout(new GridLayout(3,5));
        vorgangspanel6.setLayout(gbl6);
        hauptpanel.setLayout(gblhaupt);
        buildConstraints(gbc,0,0,1,1,1000,100);
        gbl2.setConstraints(dateieingabe,gbc);
        dateieingabe.setToolTipText("Hier den Dateinamen eingeben im Stil \"<Laufwerk>:\\<Pfad>\\<dateiname>.<endung>\" (Ohne Anführungszeichen)");
        dateipanel2.add(dateieingabe);
        buildConstraints(gbc,1,0,2,1,10,100);
        gbl2.setConstraints(dateisuchen,gbc);
        configpanel3.add(rb[8]);
        configpanel3.add(configausgabe);
        configpanel3.add(configformat);
        configpanel3.add(configtestart);
        configpanel3.add(zahlenbreite);
        configpanel3.add(rb[0]);
        configpanel3.add(rb[2]);
        configpanel3.add(rb[4]);
        configpanel3.add(rb[6]);
        bitbreiteeingabe.setToolTipText("Breite der Internen Zufallszahlen in Bit");
        configpanel3.add(bitbreiteeingabe);
        configpanel3.add(rb[1]);
        configpanel3.add(rb[3]);
        configpanel3.add(rb[5]);
        configpanel3.add(rb[7]);
        configpanel3.add(hilfe);
        buildConstraints(gbc,0,0,1,1,10,100);
        gbl6.setConstraints(vorgangsname, gbc);
        vorgangspanel6.add(vorgangsname);
        buildConstraints(gbc,1,0,2,1,70,100);
        gbl6.setConstraints(fortschritt, gbc);
        vorgangspanel6.add(fortschritt);
        buildConstraints(gbc,3,0,1,1,10,100);
        fortschritt.setToolTipText("Während des Testens wird hier Graphisch angezeigt, wieviel % schon getestet sind");
        textbox.setToolTipText("Hier werden sämtliche Meldungen des Programms chronologisch geordnet mit Zeit hineingeschrieben.");
        gbl6.setConstraints(vorgangabbruch, gbc);
        vorgangspanel6.add(vorgangabbruch);
        textpanel7 = new JScrollPane(textbox);
        textbox.append(progname + "\nProgrammstart am " + systemdatum() + " um " + systemzeit() + " Uhr\n\nBEI PROBLEMEN IN DREI STUFEN VORGEHEN:\n     1) Maustaste über das fragliche Objekt halten, Hilfe erscheint\n     2) Wenn Problem nicht geklärt, Hilfe-Button drücken\n     3) Notfalls: Technische Anfragen an zertifizierung@bsi.bund.de");
        ButtonGroup rbgrpsicherheit = new ButtonGroup();
        ButtonGroup rbgrpausgabe = new ButtonGroup();
        ButtonGroup rbgrpformat = new ButtonGroup();
        ButtonGroup rbgrpwiederholung = new ButtonGroup();
        rbgrpsicherheit.add(rb[0]);
        rbgrpsicherheit.add(rb[1]);
        rbgrpsicherheit.add(rb[8]);
        rbgrpausgabe.add(rb[2]);
        rbgrpausgabe.add(rb[3]);
        rbgrpformat.add(rb[4]);
        rbgrpformat.add(rb[5]);
        rbgrpwiederholung.add(rb[6]);
        rbgrpwiederholung.add(rb[7]);
        buildConstraints(gbc,0,0,1,1,100,5);
        gblhaupt.setConstraints(configlabel1,gbc);
        hauptpanel.add(configlabel1);
        buildConstraints(gbc,0,1,1,1,100,5);
        gblhaupt.setConstraints(dateipanel2,gbc);
        hauptpanel.add(dateipanel2);
        buildConstraints(gbc,0,2,1,1,100,20);
        gblhaupt.setConstraints(configpanel3,gbc);
        hauptpanel.add(configpanel3);
        buildConstraints(gbc,0,3,1,1,100,5);
        gblhaupt.setConstraints(vorgangslabel4,gbc);
        hauptpanel.add(vorgangslabel4);
        buildConstraints(gbc,0,4,1,1,100,200);
        gblhaupt.setConstraints(start5,gbc);
        hauptpanel.add(start5);
        buildConstraints(gbc,0,5,1,1,100,15);
        gblhaupt.setConstraints(vorgangspanel6,gbc);
        hauptpanel.add(vorgangspanel6);
        buildConstraints(gbc,0,6,1,1,100,3000);
        gblhaupt.setConstraints(textpanel7,gbc);
        hauptpanel.add(textpanel7);
        setContentPane(hauptpanel);
        elementeAnpassen(0);
        fortschritt.setValue(fortschrittzahl);
        textbox.setEditable(false);
        show();
    }
    
    
    
    public void setzefortschritt(int schritt){
        fortschritt.setValue(schritt);
    }
    
    
    
    
    
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src instanceof JButton) {
            if (src == dateisuchen){
                JFileChooser dateidialog = new JFileChooser();
                dateidialog.setDialogTitle("Datei suchen");
                if (dateidialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    dateieingabe.setText(dateidialog.getSelectedFile().getPath());
                }
            }
            if (src == start5){
                kommentar("\n\nTEST GESTARTET.",true,0);
                tester = new Tester(this);
                tester.start();
            }
            if (src == vorgangabbruch){
                skip = true;
                abbruch = true;
            }
            if (src == hilfe){
                kommentar("Hilfe wird eingeblendet - Log wird anschliessend fortgeführt.", true,0);
                textbox.append("\n\n\nHILFE von " + progname +
                "\n\nDieses Programm führt die in der mathematisch-technischen Anlage \nzur AIS 31 spezifizierten statistischen Tests durch. \nDie Hilfetexte sollen die Handhabung des Programms erleichtern. \nFür das Verständnis der Zusammenhänge wird auf die mathematisch-technischen \nAnlage zur AIS 31 verwiesen.\n" +
                "\nAnleitung:\n" +
                "   1) Geben Sie die Zufallszahlendatei mit vollständigem Pfad ein.\n" +
                "   2) Wählen Sie jeweils einen Eintrag aus den Spalten \"Tests\", \"Ausgabe\", \"Datenformat\" und \"Testart\".\n" +
                "   3) Geben Sie die Breite der internen Zufallszahlen in Bit an.\n" +
                "   4) Drücken Sie den Start-Button. Abhängig von Ihrem Rechner kann das Programm mehrere Minuten benötigen.  Der Laufzeitbalken zeigt den Fortschritt an.\n" +
                "\nTest-Suiten:\n" +
                "   P1: T0: Vgl. AIS 31, P1.i.(i) und F. Statistische Tests, Test T0\n" +
                "   P1: T1-T5: Vgl. AIS 31, P1.i.(ii-ii.c) und F. Statistische Tests, Tests T1-T5\n" +
                "   P2 - spezifisch: P2 Spezifische Tests, Vgl. AIS 31, P2.i(vii.a-vii.e) und F. Statistische Tests, Tests T6-T8; (vii.a) ~ T6a, (vii.b) ~ T6b, (vii.c) ~ T7a, (vii.d) ~ T7b, (vii.e) ~ T8\n" +
                "\nEingabe- / Dateiformat:\n" +
                "   1Byte=1RNDBit: Jedes Byte der Eingabedatei entspricht einem Zufallsbit.\n" +
                "   1Byte=8RNDBit: Jedes Byte der Eingabedatei entspricht 8 Zufallsbits,\n" +
                "   wobei das höchstwertige Bit als ältestes interpretiert wird.\n\n" +
                "Testart:\n" +
                "   Normal = Erste Durchführung des entsprechenden Tests.\n" +
                "   Wiederholung: Zweite Durchführung des entsprechenden Tests.\n\n" +
                "Interne Zufallszahlen:\n" +
                "   Breite der vom Zufallszahlengenerator erzeugten internen\n" +
                "   Zufallszahlen in Bit (vgl. AIS 31, B.1 Definitionen)\n\n" +
                "Tasten:\n" +
                "   Starttaste (Grün): Starten Sie die Tests, nachdem Sie alle Einstellungen gewählt haben.\n" +
                "   Hilfe-Taste (Gelb): Schreibt den vollständigen Hilfetext ins Textfenster.\n" +
                "\nDiejenigen Zufallszahlen der Eingabedatei, die zur Durchführung einer Testsuite \n(T0, T1-T5 oder P2-spezifische Tests) nicht benötigt werden,werden in eine neue \nDatei geschrieben. Der Dateiname ergibt sich aus der Eingabedatei durch Anhängen von \n\"_rest\". Nach Beendigung der Testsuite wird diese Datei defaultmäßig in die \nEingabezeile eingetragen.\n\n\n"
                );
                
                kommentar("Hilfe beendet - Log wird fortgeführt.", true,0);
            }
        }
    }
    
    
    
    
    
    
    
    
    
    static String systemzeit(){
        String zahlenformat = new String("00");
        DecimalFormat df = new DecimalFormat(zahlenformat);
        GregorianCalendar cal = new GregorianCalendar();
        String zeit = new String(df.format(cal.get(Calendar.HOUR_OF_DAY)) + ":" + df.format(cal.get(Calendar.MINUTE)) + ":" + df.format(cal.get(Calendar.SECOND)));
        return zeit;
    }
    
    
    
    static String systemdatum(){
        GregorianCalendar cal = new GregorianCalendar();
        String zahlenformat = new String("00");
        DecimalFormat df = new DecimalFormat(zahlenformat);
        String datum = new String(df.format(cal.get(Calendar.DAY_OF_MONTH)) + "." + df.format((cal.get(Calendar.MONTH)+1)) + "." + df.format(cal.get(Calendar.YEAR)));
        return datum;
    }
    
    
    
    public void itemStateChanged(ItemEvent evt) {
        GregorianCalendar cal = new GregorianCalendar();
        Object src = evt.getSource();
        int srcnum = 0;
        for(int i=0;i<8;i++){
            if (src == rb[i]){
                srcnum = i;
            }
        }
        switch (srcnum) {
            case 8:
            case 0:
            case 1:
                if(rb[0].isSelected()) {
                    testart = 1;
                }
                if(rb[1].isSelected()) {
                    testart = 2;
                }
                if(rb[8].isSelected()) {
                    testart = 0;
                }
                break;
            case 2:
                geschwaetzig = !(rb[2].isSelected());
                if (geschwaetzig){
                }
                else {
                }
                break;
            case 4:
                byteformat = (rb[4].isSelected());
                if (byteformat){
                }
                else {
                }
                break;
            case 6:
                normalertest = (rb[6].isSelected());
                if (normalertest){
                }
                else{
                }
                break;
        }
    }
    
    
    
    public void elementeAnpassen(int status) {
        switch (status){
            case 0:
                setTitle(progname + " - [Kein Job]");
                for (int i = 0; i<rb.length; i++){
                    rb[i].setEnabled(true);
                }
                configtest.setEnabled(true);
                configtestart.setEnabled(true);
                configlabel1.setEnabled(true);
                configausgabe.setEnabled(true);
                configformat.setEnabled(true);
                vorgangsname.setEnabled(false);
                dateieingabe.setEnabled(true);
                fortschritt.setEnabled(false);
                start5.setEnabled(true);
                vorgangabbruch.setEnabled(false);
                textbox.setEnabled(true);
                zahlenbreite.setEnabled(true);
                bitbreiteeingabe.setEnabled(true);
                hilfe.setEnabled(true);
                break;
            case 1:
                setTitle(progname + " - [Lese Datei]");
                for (int i = 0; i<rb.length; i++){
                    rb[i].setEnabled(false);
                }
                configtest.setEnabled(false);
                configausgabe.setEnabled(false);
                configtestart.setEnabled(false);
                configlabel1.setEnabled(false);
                configformat.setEnabled(false);
                vorgangsname.setEnabled(true);
                dateieingabe.setEnabled(false);
                fortschritt.setEnabled(true);
                dateisuchen.setEnabled(false);
                start5.setEnabled(false);
                vorgangabbruch.setEnabled(true);
                textbox.setEnabled(true);
                bitbreiteeingabe.setEnabled(false);
                zahlenbreite.setEnabled(false);
                hilfe.setEnabled(false);
                break;
            case 2:
                setTitle(progname + " - [Prüfung läuft]");
                for (int i = 0; i<rb.length; i++){
                    rb[i].setEnabled(false);
                }
                configtest.setEnabled(false);
                configausgabe.setEnabled(false);
                configformat.setEnabled(false);
                configtestart.setEnabled(false);
                configlabel1.setEnabled(false);
                vorgangsname.setEnabled(true);
                dateieingabe.setEnabled(false);
                fortschritt.setEnabled(true);
                dateisuchen.setEnabled(false);
                start5.setEnabled(false);
                vorgangabbruch.setEnabled(true);
                textbox.setEnabled(true);
                zahlenbreite.setEnabled(false);
                bitbreiteeingabe.setEnabled(false);
                hilfe.setEnabled(false);
                break;
        }
    }
    
    
    
    public void kommentar(String text, boolean wichtig, int ebene) {
        if ((wichtig) | (geschwaetzig)){
            textbox.append("\n[" + systemzeit() + "] ");
            if (ebene > 0){
                for (int i = 0; i< ebene; i++){
                    textbox.append("  ");
                }
            }
            textbox.append(text);
            textbox.setCaretPosition(textbox.getText().length());
            if (logfileenabled){
                log.print("[" + systemzeit() + "] ");
                if (ebene > 0){
                    for (int i = 0; i< ebene; i++){
                        log.print("  ");
                    }
                }
                log.println(text);
            }
        }
    }
    
    
    
    void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.weighty = wy;
        gbc.fill = GridBagConstraints.BOTH;
    }
    
    
    
    static void look(){
        try {
            UIManager.setLookAndFeel(
            UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e1) {
        }
    }
    
    
    
    public static void main(String[] arguments) {
        try {
            UIManager.setLookAndFeel(
            UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e1){
        }
        Evaluator evaluator = new Evaluator();
    }
    
    
    
    
}

class Tester extends Thread{
    
    Evaluator evaluator;
    final int elementzahl = 256*65536;
    public byte[] BitFeldA = new byte[256*65536];
    public byte[] BitFeldB = new byte[20000];
    static boolean geschwaetzig;
    static boolean byteformat;
    static byte testart;
    static boolean normalertest;
    static int fortschrittzahlt;
    static int bitbreite;
    static int bitzahl;
    static int c;
    int letzterwert;
    boolean zuwenigdaten = false;
    boolean abbruch = false;
    boolean skip = false;
    boolean dateigewaehlt;
    int letzteselement;
    FileInputStream datei;
    DataInputStream bitdatei;
    String progname;
    
    
    Tester(Evaluator eva){
        setPriority(NORM_PRIORITY);
        evaluator = eva;
        geschwaetzig = evaluator.geschwaetzig;
        byteformat = evaluator.byteformat;
        testart = evaluator.testart;
        normalertest = evaluator.normalertest;
        bitbreite = evaluator.bitbreite;
        bitzahl = evaluator.bitzahl;
        c = evaluator.c;
        letzterwert = evaluator.letzterwert;
        dateigewaehlt = evaluator.dateigewaehlt;
        letzteselement = evaluator.letzteselement;
        progname = evaluator.progname;
        
    }
    
    public boolean einlesen(byte[] BitFeldA) {
        
        String dateiname = evaluator.dateieingabe.getText();
        boolean eof = false;
        int anzahl = 0;
        boolean fehler = false;
        if (testart == 0){
            c = teilenaufrunden(48,bitbreite);
            bitzahl = bitbreite * c * 65536;
        }
        if (testart == 1){
            bitzahl = teilenaufrunden(257,(bitbreite + 1)) * ((teilenaufrunden(20000, bitbreite)) * bitbreite + (bitbreite * 20000));
        }
        if (testart == 2){
            bitzahl = 7200000;
        }
        try {
            datei = new FileInputStream(dateiname);
            if (!byteformat){
                int[] tempfeld = new int[teilenaufrunden(bitzahl,8) + 1];
                bitdatei = new DataInputStream(datei);
                evaluator.kommentar("Kopiere BitStream-Datei in RAM ...",false,2);
                while ((!eof) && (anzahl < (teilenaufrunden(bitzahl,8)))){
                    try {
                        tempfeld[anzahl] = bitdatei.readUnsignedByte();
                    } catch (EOFException e2){
                        eof = true;
                        evaluator.kommentar("Dateifehler: " + e2.toString(), true,2);
                        
                    }
                    anzahl++;
                }
                anzahl = anzahl * 8;
                for (int i = 0; i < BitFeldA.length; i++){
                    BitFeldA[i] = 0;
                }
                evaluator.kommentar("Konvertiere Dateidaten in ByteStream ...",false,2);
                for (int i = 0; i < (teilenaufrunden(anzahl,8)); i++){
                    if (tempfeld[i] > 127){
                        BitFeldA[(i*8)+0] = 1;
                        tempfeld[i] = tempfeld[i] - 128;
                    }
                    if (tempfeld[i] > 63){
                        BitFeldA[(i*8)+1] = 1;
                        tempfeld[i] = tempfeld[i] - 64;
                    }
                    if (tempfeld[i] > 31){
                        BitFeldA[(i*8)+2] = 1;
                        tempfeld[i] = tempfeld[i] - 32;
                    }
                    if (tempfeld[i] > 15){
                        BitFeldA[(i*8)+3] = 1;
                        tempfeld[i] = tempfeld[i] - 16;
                    }
                    if (tempfeld[i] > 7){
                        BitFeldA[(i*8)+4] = 1;
                        tempfeld[i] = tempfeld[i] - 8;
                    }
                    if (tempfeld[i] > 3){
                        BitFeldA[(i*8)+5] = 1;
                        tempfeld[i] = tempfeld[i] - 4;
                    }
                    if (tempfeld[i] > 1){
                        BitFeldA[(i*8)+6] = 1;
                        tempfeld[i] = tempfeld[i] - 2;
                    }
                    if (tempfeld[i] > 0){
                        BitFeldA[(i*8)+7] = 1;
                    }
                }
            }
            else{
                evaluator.kommentar("Kopiere ByteStream in RAM ...",false,2);
                anzahl = datei.read(BitFeldA, 0, bitzahl);
            }
        } catch (IOException e) {
            if (!dateigewaehlt){
                evaluator.kommentar("Dateifehler: Keine Datei ausgewählt!",true,2);
                return false;
            }
            else{
                evaluator.kommentar("Dateifehler: " + e.toString(), true,2);
                return false;
            }
        }
        try {
            int filepos = 0;
            evaluator.setzefortschritt(21);
            evaluator.kommentar("Schreibe Restdatei: " + dateiname + "_rest",true,2);
            FileInputStream datei = new FileInputStream(dateiname);
            FileOutputStream datei2 = new FileOutputStream(dateiname + "_rest");
            if (byteformat){
                eof = false;
                while (!eof){
                    filepos++;
                    int elementtemp;
                    elementtemp = datei.read();
                    if (elementtemp == -1){
                        eof = true;
                    } else {
                        if (filepos > teilenaufrunden(bitzahl,8)){
                            datei2.write(elementtemp);
                        }
                    }
                }
            } else {
                eof = false;
                while (!eof){
                    filepos++;
                    int elementtemp;
                    elementtemp = datei.read();
                    if (elementtemp == -1){
                        eof = true;
                    } else {
                        if (filepos > (bitzahl/8)){
                            datei2.write(elementtemp);
                        }
                    }
                }
            }
            datei.close();
            datei2.close();
        } catch (IOException e) {
            evaluator.kommentar("Dateifehler: " + e.toString(), true,2);
        }
        if (anzahl < bitzahl) {
            evaluator.kommentar("Dateifehler: Datei zu klein. Mindestgröße " + bitzahl + " Bits. Größe: " + anzahl + " Bits.", true,2);
            return false;
        }
        evaluator.kommentar(anzahl + " Elemente in RAM kopiert.",false,2);
        if (byteformat){
            evaluator.kommentar("Überprüfe Daten ...",false,2);
            for (int i = 0; i<bitzahl; i++){
                if ((BitFeldA[i] != 1) & (BitFeldA[i] != 0)){
                    evaluator.kommentar("Element Nr. " + (i) + " ungleich \"0\" oder \"1\", da \"" + BitFeldA[i] + "\" - Datenprüfung nicht bestanden.",true,2);
                    return false;
                }
            }
        }
        return true;
    }
    
    
    
    void neuesfeldb(byte[] BitFeldA, byte[] BitFeldB, int durchlauf){
        int grundelement = 0;
        
        if (durchlauf % (bitbreite+1) == 0){
            evaluator.kommentar("Teste im Block.",false,1);
            for (int i = 0; i < 20000; i++){
                BitFeldB[i] = BitFeldA[i + letzteselement + 1];
            }
            letzteselement += teilenaufrunden(20000,bitbreite)*bitbreite;
        }
        else{
            grundelement = (durchlauf % (bitbreite + 1)) + letzteselement;
            for(int i= 0; i<20000; i++) {
                BitFeldB[i] = BitFeldA[grundelement + (i * bitbreite)];
            }
            if (durchlauf % (bitbreite+1) == bitbreite) {
                letzteselement += 20000*bitbreite;
            }
            
        }
    }
    
    int teilenaufrunden(int a, int b){
        if ((a % b) == 0){
            return (int)(a/b);
        }
        else {
            return (((int)(a/b)) + 1);
        }
    }
    
    
    public void run(){
        if (test()) {
            if (evaluator.testart == 0 | evaluator.testart == 1){
                evaluator.kommentar("Durchlauf erfolgreich beendet, Restdatei zum Test ausgewählt.",true,0);
            }
            else {
                evaluator.kommentar("Durchlauf erfolgreich beendet.",true,0);
            }
        }
        else {
            evaluator.kommentar("Test wurden nicht (vollständig) durchgeführt ODER NICHT BESTANDEN.\nNeben einem Benutzerabbruch kann dies folgende Ursachen haben:\n1) Ein Test wurde nicht bestanden (richtiges Dateiformat gewählt?)\n2) Bedienungsfehler (zB Interne Zufallszahlenbreite nicht eingegeben)\n3) Dateifehler (Datei zu klein, nicht vorhanden, etc)\nBitte durchsuchen Sie das LOG um den Fehler zu finden!",true,0);
        }
        try{
            evaluator.logbuffer.flush();
        } catch (IOException e){
            evaluator.kommentar("Flush auf AIS 31.log fehlgeschlagen: " + e.toString(),true,0);
        }
    }
    
    boolean test(){
        evaluator.setzefortschritt(0);
        boolean fehler = false;
        String vergleich = new String("Breite in Bit");
        String vergleichnull = new String("");
        if (vergleich.equals(evaluator.bitbreiteeingabe.getText()) || vergleichnull.equals(evaluator.bitbreiteeingabe.getText())){
            evaluator.kommentar("Bitte Bitbreite der internen Zufallszahlen eingeben!",true,0);
            return false;
        }
        bitbreite = Integer.parseInt(evaluator.bitbreiteeingabe.getText());
        if ((bitbreite > 256) || (bitbreite < 1)){
            evaluator.kommentar("Bitbreite der internen Zufallszahlen nicht im zugelassenen Intervall [1;256]",true,0);
            return false;
        }
        if (testart == 0){
            evaluator.kommentar("TEST-SUITE:\tP1/T0",true,0);
        }
        if (testart == 1){
            evaluator.kommentar("TEST-SUITE:\tP1/T1-T5",true,0);
        }
        if (testart == 2){
            evaluator.kommentar("TEST-SUITE:\tP2 (spezifische Tests)",true,0);
        }
        evaluator.kommentar("DATEINAME:\t" + evaluator.dateieingabe.getText(),true,0);
        if (geschwaetzig){
            evaluator.kommentar("AUSGABEDETAILS:\tEingeschaltet.",true,0);
        }else{
            evaluator.kommentar("AUSGABEDETAILS:\tAusgeschaltet.",true,0);
        }
        if (byteformat){
            evaluator.kommentar("DATENFORMAT:\t1 Dateibyte enthält 1 Random-Bit.",true,0);
        }else{
            evaluator.kommentar("DATENFORMAT:\t1 Dateibyte enthält 8 Random-Bits.",true,0);
        }
        if (normalertest){
            evaluator.kommentar("TESTART:\tNormaler Test.",true,0);
        }else{
            evaluator.kommentar("TESTART:\tWiederholungstest.",true,0);
        }
        evaluator.kommentar("RND-BITBREITE:\t" + bitbreite + " bit.",true,0);
        String[] testnamen = new String[12];
        testnamen[0] = "Starte Test T0 (Disjunktheitstest); Kriterium P1.i(i)";
        testnamen[1] = "Starte Test T1 (Monobittest);  Kriterium P1.i(ii)";
        testnamen[2] = "Starte Test T2 (Pokertest);  Kriterium P1.i(ii)";
        testnamen[3] = "Starte Test T3 (Runtest);  Kriterium P1.i(ii)";
        testnamen[4] = "Starte Test T4 (Long Runtest);  Kriterium P1.i(ii)";
        testnamen[5] = "Starte Test T5 (Autokorrelationstest);  Kriterium P1.i(ii)";
        evaluator.kommentar("Datei wird eingelesen.",true,1);
        evaluator.elementeAnpassen(1);
        if (einlesen(BitFeldA)) {
            evaluator.kommentar("Datei wurde eingelesen.",false,1);
            evaluator.setzefortschritt(42);
        }
        else {
            evaluator.kommentar("Einlesen der Datei fehlgeschlagen.",true,1);
            evaluator.elementeAnpassen(0);
            evaluator.setzefortschritt(0);
            return false;
        }
        evaluator.elementeAnpassen(2);
        if (testart == 0){
            evaluator.kommentar(testnamen[0],true,1);
            if (test0(c,bitbreite)) {
                evaluator.kommentar("Test T0 bestanden.",false,1);
                evaluator.setzefortschritt(300);
                evaluator.dateieingabe.setText(evaluator.dateieingabe.getText() + "_rest");
            }
            else {
                evaluator.kommentar("Test T0 (Disjunktheitstest); Kriterium P1.i(i) nicht bestanden.",true,1);
                fehler = true;
            }
        }
        if (testart == 1){
            for (int durchlauf = 0; durchlauf < 257; durchlauf++){
                if (evaluator.abbruch){
                    evaluator.kommentar("Auf Wunsch des Users ABGEBROCHEN.",true,0);
                    evaluator.elementeAnpassen(0);
                    evaluator.abbruch = false;
                    return false;
                }
                boolean durchlauffehler = false;
                evaluator.kommentar("Durchlauf " + (durchlauf+1) + " von 257 beginnt.",true,1);
                neuesfeldb(BitFeldA,BitFeldB,durchlauf);
                evaluator.kommentar(testnamen[1],false,2);
                if (test1()) {
                    evaluator.kommentar("Test T1 bestanden.",false,2);
                }
                else {
                    evaluator.kommentar("Test T1 (Monobittest);  Kriterium P1.i(ii) nicht bestanden.",true,2);
                    durchlauffehler = true;
                }
                evaluator.kommentar(testnamen[2],false,2);
                if (test2()) {
                    evaluator.kommentar("Test T2 bestanden.",false,2);
                }
                else {
                    evaluator.kommentar("Test T2 (Pokertest);  Kriterium P1.i(ii) nicht bestanden.",true,2);
                    durchlauffehler = true;
                }
                evaluator.kommentar(testnamen[3],false,2);
                if (test3()) {
                    evaluator.kommentar("Test T3 bestanden.",false,2);
                }
                else {
                    evaluator.kommentar("Test T3 (Runtest);  Kriterium P1.i(ii) nicht bestanden.",true,2);
                    durchlauffehler = true;
                }
                evaluator.kommentar(testnamen[4],false,2);
                if (test4()) {
                    evaluator.kommentar("Test T4 bestanden.",false,2);
                }
                else {
                    evaluator.kommentar("Test T4 (Long Runtest);  Kriterium P1.i(ii) nicht bestanden.",true,2);
                    durchlauffehler = true;
                }
                evaluator.kommentar(testnamen[5],false,2);
                if (test5()) {
                    evaluator.kommentar("Test T5 bestanden.",false,2);
                }
                else {
                    evaluator.kommentar("Test T5 (Autokorrelationstest);  Kriterium P1.i(ii) nicht bestanden.",true,2);
                    durchlauffehler = true;
                }
                if (durchlauffehler){
                    evaluator.kommentar("Durchlauf " + (durchlauf+1) + " nicht bestanden.", true, 1);
                    fehler = true;
                }
                else {
                    evaluator.kommentar("Durchlauf " + (durchlauf+1) + " bestanden.", false, 1);
                }
                evaluator.setzefortschritt(durchlauf + 42);
            } 
            evaluator.setzefortschritt(300);
            evaluator.dateieingabe.setText(evaluator.dateieingabe.getText() + "_rest");
        }
        if (testart == 2){
            evaluator.kommentar("Testprozedur T6a zur Verifikation von P2.i)(vii.a) gestartet.", false, 1);
            if (test6a()){
                evaluator.kommentar("Testprozedur T6a bestanden.", false, 1);
            }
            else {
                evaluator.kommentar("Testprozedur T6a zur Verifikation von P2.i)(vii.a) nicht bestanden.",true,1);
                fehler = true;
            }
            if (zuwenigdaten) {
                evaluator.kommentar("Zuwenig Testdaten - Testreihe abgebrochen (Progamm liest maximal 7200000 Zufallsbits ein).",true,1);
                return false;
            }
            evaluator.setzefortschritt(100);
            evaluator.kommentar("Testprozedur T6b zur Verifikation von P2.i)(vii.b) gestartet.", false, 1);
            if (test6b()){
                evaluator.kommentar("Testprozedur T6b bestanden.", false, 1);
            }
            else {
                evaluator.kommentar("Testprozedur T6b zur Verifikation von P2.i)(vii.b) nicht bestanden.",true,1);
                fehler = true;
            }
            if (zuwenigdaten) {
                evaluator.kommentar("Zuwenig Testdaten - Testreihe abgebrochen (Progamm liest maximal 7200000 Zufallsbits ein).",true,1);
                return false;
            }
            evaluator.setzefortschritt(150);
            evaluator.kommentar("Testprozedur T7a zur Verifikation von P2.i)(vii.c) gestartet.", false, 1);
            if (test7a()){
                evaluator.kommentar("Testprozedur T7a bestanden.", false, 1);
            }
            else {
                evaluator.kommentar("Testprozedur T7a zur Verifikation von P2.i)(vii.c) nicht bestanden.",true,1);
                fehler = true;
            }
            if (zuwenigdaten) {
                evaluator.kommentar("Zuwenig Testdaten - Testreihe abgebrochen (Progamm liest maximal 7200000 Zufallsbits ein).",true,1);
                return false;
            }
            evaluator.setzefortschritt(200);
            evaluator.kommentar("Testprozedur T7b zur Verifikation von P2.i)(vii.d) gestartet.", false, 1);
            if (test7b()){
                evaluator.kommentar("Testprozedur T7b bestanden.", false, 1);
            }
            else {
                evaluator.kommentar("Testprozedur T7b zur Verifikation von P2.i)(vii.d) nicht bestanden.",true,1);
                fehler = true;
            }
            if (zuwenigdaten) {
                evaluator.kommentar("Zuwenig Testdaten - Testreihe abgebrochen (Progamm liest maximal 7200000 Zufallsbits ein).",true,1);
                return false;
            }
            evaluator.setzefortschritt(250);
            evaluator.kommentar("Test T8 zur Verifikation von P2.i)(vii.e) gestartet.", false, 1);
            if (test8()){
                evaluator.kommentar("Test T8 bestanden.", false, 1);
            }
            else {
                evaluator.kommentar("Test T8 zur Verifikation von P2.i)(vii.e) nicht bestanden.",true,1);
                fehler = true;
            }
            evaluator.setzefortschritt(300);
        }
        evaluator.elementeAnpassen(0);
        return !fehler;
    }
    boolean test0(int c, int bitbreite){
        int i,j,einsen;
        boolean ok;
        long u0= (long)0;
        long u1= (long)1;
        long[] WFeld = new long[65536];
        for(i=0; i<65536; i++){
            WFeld[i]=u0;
            for(j=0; j<48; j++) WFeld[i]+=(u1<<j)*BitFeldA[(c*bitbreite)*i+j];
        }
        Arrays.sort(WFeld);
        ok=true;
        for(i=1; i<65536; i++) {
            if (WFeld[i-1]==WFeld[i]) {
                ok=false;
            }
        }
        return ok;
    }
    
    
    
    boolean test1(){
        int i, einsen, mg=20000, ptest=0;
        boolean ok;
        einsen=0;
        for(i=0; i<mg; i++) {
            einsen+=BitFeldB[i];
        }
        if ((einsen>9654) && (einsen<10346)) ok=true;
        else ok=false;
        evaluator.kommentar("Anzahl Einsen: " + einsen, false,3);
        evaluator.kommentar("Zulässiger Bereich: [9655; 10345]", false,2);
        return ok;
    }
    
    
    
    boolean test2(){
        int i,j,index,ptest;
        int[] Hfg = new int[16];
        boolean ok;
        double testgroesse;
        
        for(i=0; i<16; i++) Hfg[i]=0;
        for(i=0; i<5000; i++) {
            index=0;
            for(j=0; j<4; j++) index+=(1<<j)*BitFeldB[4*i+j];
            Hfg[index]++;
        }
        testgroesse=0.0;
        for(i=0; i<16; i++) testgroesse+=Hfg[i]*Hfg[i];
        testgroesse=testgroesse*(16.0/5000.0)-5000.0;
        if ((testgroesse>1.03) && (testgroesse<57.4)) ok=true;
        else ok=false;
        if (geschwaetzig){
            evaluator.kommentar("Testgroesse = " + testgroesse, false,3);
        }
        return ok;
    }
    
    
    
    boolean test3(){
        int i,j,run,ptest=0;
        int[] Run0Feld = new int[7];
        int[] Run1Feld = new int[7];
        boolean ok;
        int UGrenze[]={0,2267,1079,502,223,90,90};
        int OGrenze[]={0,2733,1421,748,402,223,223};
        for(i=0; i<7; i++) Run0Feld[i]=Run1Feld[i]=0;
        run=1;
        for(i=1; i<20000; i++) {
            if (BitFeldB[i-1]==BitFeldB[i]) run++;
            else {
                if (run>6) run=6;
                if (BitFeldB[i-1]==1)
                    Run1Feld[run]++;
                else Run0Feld[run]++;
                run=1;
            }
        }
        if (run>6) run=6;
        if (BitFeldB[i-1]==1)
            Run1Feld[run]++;
        else Run0Feld[run]++;
        ok=true;
        for(i=1; i<=6; i++){
            if ( (Run0Feld[i]>=UGrenze[i]) && (Run0Feld[i]<=OGrenze[i]) ){
                evaluator.kommentar("0-Runs[" + i + "] = " + Run0Feld[i] + "; zulässiges Intervall: [" + UGrenze[i] + "; " + OGrenze[i] + "]",false,3);
            }
            else {
                ok=false;
                evaluator.kommentar("FEHLER: 0-Runs[" + i + "] = " + Run0Feld[i] + "; zulässiges Intervall: [" + UGrenze[i] + "; " + OGrenze[i] + "]",true,3);
            }
            if ( (Run1Feld[i]>=UGrenze[i]) && (Run1Feld[i]<=OGrenze[i]) ){
                evaluator.kommentar("1-Runs[" + i + "] = " + Run1Feld[i] + "; zulässiges Intervall: [" + UGrenze[i] + "; " + OGrenze[i] + "]",false,3);
            }
            else {
                ok=false;
                evaluator.kommentar("FEHLER: 1-Runs[" + i + "] = " + Run1Feld[i] + "; zulässiges Intervall: [" + UGrenze[i] + "; " + OGrenze[i] + "]",true,3);
            }
        }
        return ok;
    }
    
    
    
    boolean test4(){
        int i,run,ptest=0;
        boolean ok;
        
        run=1;ok=true;
        
        for(i=1; i<20000; i++) {
            if (BitFeldB[i-1]==BitFeldB[i]) {
                run++;
                if (run>=34) {
                    ok=false;
                    evaluator.kommentar("Long Run aufgetreten (Wert: " + BitFeldB[i] + "). Erste Bitposition: " + (i-33) + ".",true,2);
                }
            }
            else run=1;
        }
        return ok;
    }
    
    
    
    boolean test5(){
        int i,j,k,tau,Z_tau,max,maxindex,ptest=0;
        boolean ok;
        int[] ShiftFeld = new int[5000];
        int[] MaxKorrFeld = new int[5000];
        
        for(tau=1; tau<=5000; tau++) {
            Z_tau=0;
            for(i=0; i<5000; i++) Z_tau+=(BitFeldB[i]^BitFeldB[i+tau]);
            ShiftFeld[tau-1]=Z_tau;
        }
        
        max=0;
        
        for(tau=0; tau<5000; tau++) {
            if (Math.abs(ShiftFeld[tau]-2500)>max){
                max=Math.abs(ShiftFeld[tau]-2500);
            }
        }
        
        j=0;
        for(tau=0; tau<5000; tau++) {
            if (Math.abs(ShiftFeld[tau]-2500)==max){
                MaxKorrFeld[j]=tau;
                j++;
            }
        }
        
        evaluator.kommentar("Maximale Z_tau-Abweichung von 2500: " + max,false,3);
        evaluator.kommentar("Aufgetreten für Shifts: ",false,3);
        for(k=0; k<j; k++){
            evaluator.kommentar("Shift: " + (MaxKorrFeld[k]+1),false,4);
        }
        tau=MaxKorrFeld[0];
        Z_tau=0;
        for(i=10000; i<15000; i++){
            Z_tau+=(BitFeldB[i]^BitFeldB[i+tau+1]);
        }
        tau++;
        evaluator.kommentar("Nochmaliger Autokorrelationstest mit Shift: " + tau + " auf Bits 10.000 bis 14.999",false,3);
        evaluator.kommentar("Z_" + tau + " = " + Z_tau,false,3);
        if (( Z_tau > 2326) && ( Z_tau< 2674))
            ok=true;
        else
            ok=false;
        return ok;
    }
    
    
    boolean test6a(){
        boolean ok;
        double[] prob = new double[1];
        double absdiff = 0.0;
        int groesse=100000;
        int einsen=0;
        for(int i=0; i<groesse ; i++) einsen+=BitFeldA[i];
        prob[0]=(double) einsen / (double) groesse;
        absdiff=Math.abs(prob[0]-0.5);
        if ((absdiff<0.025)) ok = true;  
        else ok = false;
        letzterwert = groesse;
        evaluator.kommentar("|P(1) - 0.5| = " + absdiff,false,2);
        evaluator.kommentar("Letztes Element: " + letzterwert,false,2);
        return ok;
    }
    
    
    boolean test6b(){
        int groesse=100000;
        int[] counter = {0,0};
        int einsen[] = {0,0};
        int voll[] = {0,0};
        int i=0;
        double[] prob = new double[2];
        double absdiff = 0.0;
        while ((voll[0]+voll[1]<2) && ((letzterwert+2*i+1)<bitzahl)){
            if(voll[BitFeldA[letzterwert+2*i]] == 1);
            else {
                counter[BitFeldA[letzterwert+2*i]]+=1;
                if (counter[BitFeldA[letzterwert+2*i]]==groesse) voll[BitFeldA[letzterwert+2*i]]=1;
                einsen[BitFeldA[letzterwert+2*i]]+=BitFeldA[letzterwert+2*i+1];
            }
            i++;
        }  
        letzterwert += 2*i;
        if (voll[0]+voll[1]<2){
            evaluator.kommentar("Inputdatei zu klein....Kriterium P2.i)(vii.b) konnte nicht geprueft werden.",true,2);
            zuwenigdaten = true;
            return false;
        }
        else {
            
            for(i=0; i<2; i++) prob[i]=(double)einsen[i]/groesse;
            absdiff=Math.abs(prob[0]-prob[1]);
            evaluator.kommentar("p(01) = " + prob[0],false,2);
            evaluator.kommentar("p(11) = " + prob[1],false,2);
            evaluator.kommentar("|p_(01) - p_(11)| = " + absdiff,false,2);
            evaluator.kommentar("Letztes Element: " + letzterwert,false,2);
            if (absdiff<0.02) return true;  
            else return false;
        } 
    }
    
    boolean test7a(){
        int groesse=100000;
        int hilf;
        int[] voll = {0,0,0,0};
        int[] counter = {0,0,0,0};
        int[] einsen = {0,0,0,0};
        int[] nullen = {0,0,0,0};
        double sum = 0;
        int i=0;
        while ((voll[0]+voll[1]+voll[2]+voll[3]<4) && ((letzterwert+3*i)<bitzahl)){
            hilf=2*BitFeldA[letzterwert+3*i+1]+BitFeldA[letzterwert+3*i];
            if(voll[hilf] == 1);
            else {
                counter[hilf]+=1;
                if (counter[hilf]==groesse) voll[hilf]=1;
                einsen[hilf]+=BitFeldA[letzterwert+3*i+2];
            }
            i++;
        }  
        letzterwert += 3*i;
        if (voll[0]+voll[1]+voll[2]+voll[3]<4){
            evaluator.kommentar("Inputdatei zu klein....Kriterium P2.i)(vii.c) konnte nicht geprueft werden",true,2);
            zuwenigdaten = true;
            return false;
        }
        else{
            for(i=0; i<4; i++) nullen[i]=groesse-einsen[i];
            boolean ok = true;
            for(i=0; i<2; i++) {
                sum=((double)((nullen[2*i]-nullen[2*i+1])*(nullen[2*i]-nullen[2*i+1]))/(nullen[2*i]+nullen[2*i+1])
                + (double)((einsen[2*i]-einsen[2*i+1])*(einsen[2*i]-einsen[2*i+1]))/(einsen[2*i]+einsen[2*i+1]));
                if(sum>15.13) ok = false;
                evaluator.kommentar("Testgröße[" + i + "] = " + sum,false,2);
            }
            evaluator.kommentar("Letztes Element: " + letzterwert,false,2);
            return ok;
        }
    }

    
    boolean test7b(){
        boolean ok;
        int hilf;
        int[] voll = {0,0,0,0,0,0,0,0};
        int[] counter = {0,0,0,0,0,0,0,0};
        int[] einsen = {0,0,0,0,0,0,0,0};
        int[] nullen = {0,0,0,0,0,0,0,0};
        double sum = 0;
        int groesse=100000;
        int i=0;
        while ((voll[0]+voll[1]+voll[2]+voll[3]+voll[4]+voll[5]+voll[6]+voll[7]<8) && ((letzterwert+4*i)<bitzahl)){
            hilf=4*BitFeldA[letzterwert+4*i+2]+2*BitFeldA[letzterwert+4*i+1]+BitFeldA[letzterwert+4*i];
            if(voll[hilf] == 1);
            else {
                counter[hilf]+=1;
                if (counter[hilf]==groesse) voll[hilf]=1;
                einsen[hilf]+=BitFeldA[letzterwert+4*i+3];
            }
            i++;
        }  
        letzterwert += 4*i;
        if (voll[0]+voll[1]+voll[2]+voll[3]+voll[4]+voll[5]+voll[6]+voll[7]<8){
            evaluator.kommentar("Inputdatei zu klein....Kriterium P2.i)(vii.d) konnte nicht geprueft werden",true,2);
            zuwenigdaten = true;
            ok = false;
        }
        else{
            for(i=0; i<8; i++) nullen[i]=groesse-einsen[i];
            ok=true;
            for(i=0; i<4; i++) {
                sum=(double)((nullen[2*i]-nullen[2*i+1])*(nullen[2*i]-nullen[2*i+1]))/(nullen[2*i]+nullen[2*i+1])
                + (double)((einsen[2*i]-einsen[2*i+1])*(einsen[2*i]-einsen[2*i+1]))/(einsen[2*i]+einsen[2*i+1]);
                if(sum>15.13) ok=false;
                evaluator.kommentar("Testgröße[" + i + "] = " + sum,false,2);
            }
        }
        evaluator.kommentar("Letztes Element: " + letzterwert,false,2);
        return ok;
    }
    
    
    
    boolean test8(){
        
        final int L = 8;
        final int Q = 2560; 
        final int K = 256000; 
        int hilf = 0;
        int counter = 0;
        int[] letzteposition = new int[256];
        for (int i = 0; i<256; i++){
            letzteposition[i] = -1;
        }
        int[] W = new int[Q+K];  
        double TG;
        double[] G = new double[K+Q+1];
        int abstand;
        
        G[1]=0.0;
        for(int i=1; i<=K+Q-1; i++) G[i+1]=G[i]+1.0/(i);
        for(int i=1; i<=K+Q; i++) G[i]/=Math.log(2.0);
        for (int i = 0; i<Q; i++){
            hilf = 0;
            for (int j = 0; j<8; j++){
                hilf+= BitFeldA[letzterwert+8*i+j]<<j;
            }
            letzteposition[hilf] = i;
        }
        TG=0.0;
        for(int i=Q; i<K+Q; i++){  
            hilf = 0;
            for (int j = 0; j<8; j++){
                hilf+= BitFeldA[letzterwert+8*i+j]<<j;
            }
            abstand = i - letzteposition[hilf];
            letzteposition[hilf] = i;
            TG+=G[abstand];
        } 
        TG/=(double)K;
        evaluator.kommentar("Testgröße = " + TG,false,2);
        letzterwert += 8*(2560+256000);
        evaluator.kommentar("Letztes Element: " + letzterwert,false,2);
        if (TG>7.976) return true;  
        else return false;
    }
}


