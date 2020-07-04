# AIS 31 Test Suite from BSI, Version 1.0

## a.k.a. AIS 31 (V1) Referenzimplementierung - v1.0 

By the German Federal Information Security Agency (Bundesamt für Sicherheit 
in der Informationstechnik, abbreviated as BSI) back in 2004.

AIS-31 is used in Common Criteria evaluation of hardware entropy sources.
Its American FIPS 140-2/3 counterpart is NIST SP 800-90B, for which source 
code is available at: https://github.com/usnistgov/SP800-90B_EntropyAssessment 

AIS 31 specifications (in English) can be found here:
https://www.bsi.bund.de/SharedDocs/Downloads/DE/BSI/Zertifizierung/Interpretationen/AIS_31_Functionality_classes_for_random_number_generators_e.html

The original zip file (`AIS_31_testsuit.zip`) is included here as a reference. 
```
da8165832251290d57316ce68ca0513a8f65c64680d617cbff094008641f86a3  AIS_31_testsuit.zip
e9e39a3a0ea5d996415d40981944736126aaace788f8339238d65d514f33c53b  Evaluator.java
```
Orignal files are dated in March 2004 and were downloaded on July 7, 2020 from:
https://www.bsi.bund.de/SharedDocs/Downloads/DE/BSI/Zertifizierung/Interpretationen/AIS_31_testsuit_zip

On linux the preliminaries are (at least for Ubuntu 20.04) installed with
```
# sudo apt install default-jdk 
```

The Java source file uses in Latin-1 (8859-1) encoding whereas nowadays UTF-8
encoding is expected. The source code has a ton of German umlauts etc so
this matters. You may convert it in some text editor or use an encoding flag:
```
$ javac -encoding Latin1 Evaluator.java
Note: Evaluator.java uses or overrides a deprecated API.
Note: Recompile with -Xlint:deprecation for details.
```
Those warnings are okay.

Executing it:
```
$ java Evaluator
```

Input filename goes on top ("In dieses Feld datienamen.."), the bits/sample
goes into the "Breite in Bit" and then there's a bunch of switches to play
with. The input file needs to have 5,220,000 bits (how many bytes -- that
depends on your encoding). Then hit the big green START!

You can find logs in file `AIS 31.log` (yes, the filename has a space).

Good luck and may your tests be successful!

Cheers,
- markku


Original file `readme_AIS31_Testsuite.txt`:
```
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
```
