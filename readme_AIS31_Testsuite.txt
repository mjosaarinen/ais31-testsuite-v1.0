Anleitung zum Start der AIS 31-Implementierung in JAVA:
-------------------------------------------------------


Voraussetzungen:

   - JavaVM ab Version 1.3.1 ist installiert
     (Diese oder eine neuere Version gibt es auf java.sun.com zum Download;
     hier Java2SE ausw�hlen und herunterladen)
   - Datei "Tester.class"
   - Datei "Evaluator.class" (Beide Dateien m�ssen sich in demselben 
                              Verzeichnis befinden.)


Start unter Microsoft Windows:

   In der Regel erstellt die JavaVM bei ihrer Installation einige Systemvariablen,
   die es erm�glichen, die Datei "java.exe" in der Kommandozeile von jedem Ort
   auszuf�hren. Sollte das bei Ihnen NICHT funktionieren,
   - Suchen Sie bitte die Datei "java.exe" mit dem Windows-Suchprogramm
   - �ffnen Sie die Kommandozeile
     (unter Windows95/98/Me im Men� Start -> Ausf�hren -> "command.com", 
      unter WindowsNT/2000/XP im Men� Start -> Ausf�hren -> "cmd")
   - Starten Sie das Programm mit dem folgenden Befehl:

	java.exe -classpath "[VERZEICHNIS DER CLASSDATEIEN]" Evaluator

   Befinden sich die beiden Classdateien beispielsweise im Ordner "Ais31" auf der
   Partition "C:", so lautet der Befehl folgendermassen:

	java.exe -classpath "C:\Ais31" Evaluator

   Achten Sie UNBEDINGT beim Wort "Evaluator" darauf, da� der erste Buchstabe
   gro� und die restlichen klein sind. Ansonsten startet das Programm nicht.

   Selbstverst�ndlich k�nnen Sie auch gleich das java.exe-Kommando in den "Ausf�hren"-
   Dialog eingeben. Allerdings sehen Sie dann zumindest unter WindowsNT/2000/XP 
   eventuelle Kommandozeilenfehlerausgaben nicht, da diese Systeme das 
   Kommandozeilenprogramm nach dessen Ende sofort wegblenden.

   Selbstverst�ndlich m�ssen Sie diese Prozedur nicht bei jedem Aufruf des Testprogramms
   wiederholen. Sie k�nnen den oben beschriebenen Befehl auch in eine Batchdatei oder 
   eine Verkn�pfung schreiben und die Anwendung mit einem Doppelklick ausf�hren.



Erste Schritte:
---------------

Nachdem Sie das Programm wie oben beschrieben aufgerufen haben, m�ssen Sie 
vor dessen Ausf�hrung die folgenden Einstellungen vornehmen:

   Geben Sie in die Eingabezeile den Namen und den Pfad der Datei an, die die
   zu testenden Zufallszahlen enth�lt.

   W�hlen Sie eine der drei Testklassen aus (P1:T0; P1:T1-T5; P2-spezifisch).

   W�hlen Sie die detaillierte oder die normale Ausgabeformat aus.

   Geben Sie an, in welcher Form die Zufallszahlen in der zu testenden Datei gespeichert 
   sind. Es stehen zwei M�glichkeiten zur Auswahl: 
   - Die Bin�rdarstellung jedes Zufallbytes wird als eine Folge von 8 Zufallsbits 
     interpretiert. Das h�chstwertigste Bit wird als �ltestes Bit interpretiert.
   - Die Datei enth�lt eine Folge von Nullen und Einsen. Dann entspricht jedes Zufallsbyte
     einem Zufallsbit.
   Anmerkung: Beachten Sie, dass das Programm eine Mindestanzahl an Zufallsbits
   zur Durchf�hrung der Tests ben�tigt. Ist die Einstellung "1 Zufallsbit pro Byte" gew�hlt,
   muss die einzulesende Datei entsprechend l�nger sein.

   Geben Sie an, ob der Test erstmals durchgef�hrt wird (normaler Test), oder ob es sich
   um einen Wiederholungstest handelt.
   Anmerkung: Jedes Zufallsbit wird nur einmal getestet.
   Die �brigen Zufallsbits der Eingabedatei werden in einer neuen Datei
   gespeichert.
   F�r den n�chsten Test verwendet das Programm defaultm��ig diese Restdatei.

   Geben Sie die Breite der internen Zufallszahlen in Bit an (z.B. 8, 16 oder 64).

   Starten Sie die statistischen Tests, indem Sie das Startfeld anklicken. 
   Die Testergebnisse werden im Ausgabebereich angezeigt.
