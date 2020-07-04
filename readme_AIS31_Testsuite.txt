Anleitung zum Start der AIS 31-Implementierung in JAVA:
-------------------------------------------------------


Voraussetzungen:

   - JavaVM ab Version 1.3.1 ist installiert
     (Diese oder eine neuere Version gibt es auf java.sun.com zum Download;
     hier Java2SE auswählen und herunterladen)
   - Datei "Tester.class"
   - Datei "Evaluator.class" (Beide Dateien müssen sich in demselben 
                              Verzeichnis befinden.)


Start unter Microsoft Windows:

   In der Regel erstellt die JavaVM bei ihrer Installation einige Systemvariablen,
   die es ermöglichen, die Datei "java.exe" in der Kommandozeile von jedem Ort
   auszuführen. Sollte das bei Ihnen NICHT funktionieren,
   - Suchen Sie bitte die Datei "java.exe" mit dem Windows-Suchprogramm
   - Öffnen Sie die Kommandozeile
     (unter Windows95/98/Me im Menü Start -> Ausführen -> "command.com", 
      unter WindowsNT/2000/XP im Menü Start -> Ausführen -> "cmd")
   - Starten Sie das Programm mit dem folgenden Befehl:

	java.exe -classpath "[VERZEICHNIS DER CLASSDATEIEN]" Evaluator

   Befinden sich die beiden Classdateien beispielsweise im Ordner "Ais31" auf der
   Partition "C:", so lautet der Befehl folgendermassen:

	java.exe -classpath "C:\Ais31" Evaluator

   Achten Sie UNBEDINGT beim Wort "Evaluator" darauf, daß der erste Buchstabe
   groß und die restlichen klein sind. Ansonsten startet das Programm nicht.

   Selbstverständlich können Sie auch gleich das java.exe-Kommando in den "Ausführen"-
   Dialog eingeben. Allerdings sehen Sie dann zumindest unter WindowsNT/2000/XP 
   eventuelle Kommandozeilenfehlerausgaben nicht, da diese Systeme das 
   Kommandozeilenprogramm nach dessen Ende sofort wegblenden.

   Selbstverständlich müssen Sie diese Prozedur nicht bei jedem Aufruf des Testprogramms
   wiederholen. Sie können den oben beschriebenen Befehl auch in eine Batchdatei oder 
   eine Verknüpfung schreiben und die Anwendung mit einem Doppelklick ausführen.



Erste Schritte:
---------------

Nachdem Sie das Programm wie oben beschrieben aufgerufen haben, müssen Sie 
vor dessen Ausführung die folgenden Einstellungen vornehmen:

   Geben Sie in die Eingabezeile den Namen und den Pfad der Datei an, die die
   zu testenden Zufallszahlen enthält.

   Wählen Sie eine der drei Testklassen aus (P1:T0; P1:T1-T5; P2-spezifisch).

   Wählen Sie die detaillierte oder die normale Ausgabeformat aus.

   Geben Sie an, in welcher Form die Zufallszahlen in der zu testenden Datei gespeichert 
   sind. Es stehen zwei Möglichkeiten zur Auswahl: 
   - Die Binärdarstellung jedes Zufallbytes wird als eine Folge von 8 Zufallsbits 
     interpretiert. Das höchstwertigste Bit wird als ältestes Bit interpretiert.
   - Die Datei enthält eine Folge von Nullen und Einsen. Dann entspricht jedes Zufallsbyte
     einem Zufallsbit.
   Anmerkung: Beachten Sie, dass das Programm eine Mindestanzahl an Zufallsbits
   zur Durchführung der Tests benötigt. Ist die Einstellung "1 Zufallsbit pro Byte" gewählt,
   muss die einzulesende Datei entsprechend länger sein.

   Geben Sie an, ob der Test erstmals durchgeführt wird (normaler Test), oder ob es sich
   um einen Wiederholungstest handelt.
   Anmerkung: Jedes Zufallsbit wird nur einmal getestet.
   Die übrigen Zufallsbits der Eingabedatei werden in einer neuen Datei
   gespeichert.
   Für den nächsten Test verwendet das Programm defaultmäßig diese Restdatei.

   Geben Sie die Breite der internen Zufallszahlen in Bit an (z.B. 8, 16 oder 64).

   Starten Sie die statistischen Tests, indem Sie das Startfeld anklicken. 
   Die Testergebnisse werden im Ausgabebereich angezeigt.
