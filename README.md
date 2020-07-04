# AIS 31 Test Suite from BSI, Version 1.0

## a.k.a. AIS 31 (V1) Referenzimplementierung - v1.0 

By the German Federal Information Security Agency (Bundesamt f�r Sicherheit 
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
```
