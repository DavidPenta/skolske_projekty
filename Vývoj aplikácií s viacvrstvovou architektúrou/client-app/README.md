# client-app
Front end: JavaFX

Back-end: Java 11

Session management: Library Java Preferencies

Music Player: javax.sound.sampled; io, lombok; time, nio

Manazer zavisloti: Maven

GUI: SceneBuilder

Collections: FXCollections, ObservableList

Lokalizacia: ResourceBundler, Locale, net.URLClassLoader, net.URL

Logging: JavaX.xml, javaio, javaTime, org.w3c.dom

Pre spustenie programu sluzi JAR subor client_with_dependencies

Pre zobrazenie JavaDoc o klientovi je JAR client_java_doc

# Projekt musi obsahovat

1. Kolekcie (treba vybrať vhodné dátové štruktúry podľa povahy/architektúry projektu)
  prevazne v baliku collections ArrayList<U> U = {Song, User, Album, ...} na naplnanie dat do TableViews pomocou ObservableList<U>, HashMap<key, String> pre vytvaranie   playlist zoznamov
  
2. Logovanie (Logovanie biznis logiky aplikácie + Logovanie Exceptions/Errors)
  v baliku sessionManagement MyLogger.java a vsade v kode kde nieco dolezite zaznamenavame
  
3. Lokalizácia (Preklady a lokalizácia ENG a SK)
  Localozator.java a vresources Bundles
  
4. XML (Použitá 1 vybraná XML technológia, spracovanie/parsovanie XML dokumentov, import/export do XML, SAX, DOM, StAX, JAXB, XStream, Jackson XML, XPATH, XQUERY)
  Pouzite pri implementacii logovania, parsovanie, DOM... v MyLogger.java v sessionmamangemente 
  
5. Regulárne výrazy (Vyhľadávanie s prepínačmi, filtrovanie)
  Pri vyhladavani podla subStringov, implementovane na serveri (vpesnicky, albumy,umelci, pouzivatelia, pouzivatelov), overovannie udajov pri registracii ako je         zlozitost a spravny format GUI RegisterScreen.java
  
6. JDBC (Pripojenie na vybranú databázu Derby/MySQL/PostgreSQL/SQLite/Oracle Database, ukladanie dát do DB), môžu sa použiť aj NoSQL databázy za predpokladu, že použijete JDBC napr. MongoDB
  Implementovane na serveri, pomocou metod v ServerApi sa pripojime na endpoint v api na serveri kde sa porovnavaju url a ak sa zhoduje tak dana metoda zbehne a pomococou hodnot v argumentoch metody z ktorej robime request vykoname sql na serveri (kazdy sql dopyt je vo svojom vlastnom subore)
  
7. Ošetrenie/validácia vstupov + bezpečnosť (Ochrana voči základným SQL injekciám)
  Kazdy sql dopyt je osetreny voci injekciam pomocou prepared statements a teda v sql prikazoch nie su nikdy hodnoty ako premenne ale ako symbol ?, ktore urcujeme na serveri v jednotlivych triedach (argumenty metod z klienta) zavolanie a vybavenie sql query vykoname pomocou metody update alebo query, ktore su na serveri definovane a implementovane v kazdej triede v baliku Auth
  CLIENT: Zavolanie metody, ktora je implementovana v triede ServerAPI, do argumentov vlozime hodnoty, ktore chceme pouzit v sql prikazoch, v metode je specifikovane url pomocou ktorej rozlisujeme ktora metoda v ktorej class na serveri sa ma spustit a poziadavku vybavit. Na serveri v danej triede zavolame metodu query alebo update do ktorej vlozime hodnoty z metody do PrepareCallBack a vlozime cestu k sql suboru, ktore su v resources
  Osetrujeme aj vstupy pri registracii a prihlaseni aj v nastaveniach aby sme sa vyhli roznym exceptions
  
8.GUI aplikácia (Swing, JavaFX, Vaadin), nie Android aplikácia
  JAVAFX implementovane pomocou Maven a pouzite SceneBuilder
  
9.Aplikácia by mala byť navrhnutá pre 3 rôznych používateľov/aktérov napr. admin, power/super user, používateľ (user)
Dátové zložky musia byť zapúzdrené (všetky private)
  Vsetky zlozky su private a mame 4 roznych pouzivatelov


# Obsah balikov a suborov

Root directory
===================================================================
client.iml
==========
Pridane libraries pomocou Maven nastroja

myOwnLog.xml
=========
Xml subor na zapisovanie logov pouzivania programu

pom.xml
========
Zoznam dependencies, pluginov a informacie o kode


serve.json
==========
Informacie o serveri na ktorom bezi nasa API, pripojenie

songs
==========
tu stahujeme pesnicky, ktore chceme prehrat v nasom prehravaci
Meno pesnicky je jej ID v databaze

src.main.resources
=======================================================================
fxml subory predstavujuce screeny pouzivatelskeho rozhrania vytvorene pomocou SceneBuilder
TableViewCss.css je stylovaci subor pre vsetky tabulky TableViews v programe
Bundle pre lokalizaciu. Zaznamenanie vsetkych retazcov v programe

src.main.com.musicverse.client
========================================================================
api ServerApi.java
===========
* ServerAPI trieda obsahuje pripojenie na server, kde je nasa API.
 * Metody, ktore sa tu nachadzaju sluzia na prepojenie s API endpointmi na serveri. Volaju requesty na server API pomocou HTTP requestov
 * a obdrziavaju response spolu s datami. V argumentoch maju hodnoty, s ktorymi sa budeme dopytovat na db
 * Ak potrebujeme vraciat udaje z db tak metoda ma navratovu hodnotu typu JSONNode a ak robime zapis do db a nepotrebujeme
 * nic vraciat tak metoda ma navratovu hodnotu boolean
 * Do payload ObjectNode vkladame udaje, ktore ptrebujeme v nasom requeste a priradujeme im kluce

collection.eventHandlers
=======
nakoniec nepouzitie metody na interakciu s polozkami v tableViews

collections.itemActions
=======
2 triedy na interakciu s tableViews albumov a songov, ktore implementuju interface
Implementaciu metody z daneho interface volame z triedy daneho pouzivatelskeho rozhrania (obrazovky) a vraciame vygenerovane dropdown menu s moznostiami podla prihlaseneho pouzivatela a podla zobrazenej polozky a to odkial bol screen zavolany

collection Controller.java
======
 * genericka metoda Controller sluzi na vygenerovanie TableView Albums alebo Songs alebo Artists alebo Users alebo Requests.
 * Podla generika U.
 * @param <U> zadany objekt z package Objects
  
Utills.java
=========
  createPlaylist:
     * metoda na vygenerovanie hash mapy ktora je vlozena do ListView playlistov
     * @param playlists je zoznam playlistov ktore chceme vlozit do hashmapy
     * @return vratime hashmapu naplnenu playlistami a ich ID
  
 generateTableSongs:    
     * metoda na naplenenie arraylistu pesnickami a nasledne je arraylist zaslany na naplnenie tableView pomocou
     * class Controller
     * @param tableView ktory chceme naplnit
     * @param songs2 data, ktorymi chceme plnit
     * @param mainPane vlastnik stage
     * @return vyplneny tableView
     
GUI
======
Triedy obsluhujuce fxml subory a riadenie ich funkctionalit
  
Objdects
======
Pouzite objekty v programe
  
player
=====
implementacia prehravaca a vsetkych jeho funkcionalit. Tieto funkcionality boli potom implementvane v pouzivatelskom rozhrani prehrava v triede MediaPlayerScreen v GUI baliku
  
SessionPreferencies
=========
Obsahuje 2 triedy. jedna je na implementaciu naseho logovacieho nastroja a druha na implementaciu user managementu do ktorej vlozime udaje po prihlaseni a tieto udaje su nam k dispozici pocas celeho behu programu a aj po opatovnom vypnuti zapnuti a teda pokial sa pouzivatel neodhlasi tak sa pri dalsom spustani programu nemusi znova prihlasovat. Tieto udaje su ulozene v xml subore
  
IOUtil
=======
obsahuje funkcie na prenos dat suborov
  
InitScreenFunctions
=====
metody v tejto triede sluzia na prepinanie medzi obrazovkami
* Pomocou FXMLLoader ziskame fxml subor so obrazovkou
* Preberieme Stage od aktualneho vlastnika pomocou ActionEvents alebo AnchorPane
* a do noveho Stage nahrame novy screen
  

