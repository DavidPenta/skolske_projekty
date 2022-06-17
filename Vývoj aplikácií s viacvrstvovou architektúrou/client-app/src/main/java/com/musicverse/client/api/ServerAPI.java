package com.musicverse.client.api;

import com.falsepattern.json.node.JsonNode;
import com.falsepattern.json.node.ObjectNode;
import com.musicverse.client.IOUtil;
import com.musicverse.client.sessionManagement.MyLogger;

import com.musicverse.client.sessionManagement.PreferencesLogin;
import lombok.SneakyThrows;

import lombok.val;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * ServerAPI trieda obsahuje pripojenie na server, kde je nasa API.
 * Metody, ktore sa tu nachadzaju sluzia na prepojenie s API endpointmi na serveri. Volaju requesty na server API pomocou HTTP requestov
 * a obdrziavaju response spolu s datami. V argumentoch maju hodnoty, s ktorymi sa budeme dopytovat na db
 * Ak potrebujeme vraciat udaje z db tak metoda ma navratovu hodnotu typu JSONNode a ak robime zapis do db a nepotrebujeme
 * nic vraciat tak metoda ma navratovu hodnotu boolean
 * Do payload ObjectNode vkladame udaje, ktore ptrebujeme v nasom requeste a priradujeme im kluce
 *
 */
public class ServerAPI {
    private static ServerAPI instance;

    public static ServerAPI getInstance() {
        if (instance == null) {
            instance = new ServerAPI();
        }
        return instance;
    }

    private String hostname;
    private int port;

    private ServerAPI() {
        this.connect();
    }

    @SneakyThrows
    public void connect() {
        val cfg = JsonNode.parse(Files.readString(Path.of(".", "server.json")));
        hostname = cfg.getString("hostname");
        port = cfg.getInt("port");
        //TODO load this from a config file. Using localhost for now.
        hostname = "https://musicverse-vava.xyz";
        port = 443;
    }

    public boolean registerUser(String email, String username, String password, int role) {
        val payload = new ObjectNode();
        payload.set("email", email);
        payload.set("username", username);
        payload.set("password", password);
        payload.set("access_level", role);
        if (queryServerJson("register", Method.POST, payload, (code, response) -> response.getString("status").equals("ok"))){
            new MyLogger("Registracia interpreta: "+username,"INFO");
            return createArtist(email);

        }
        new MyLogger("Chyba pri registracii interpreta","ERROR");
        return false;

    }

    public JsonNode searchUser(String input){
        val payload = new ObjectNode();
        payload.set("input", input);
        return queryServerJson("searchUsers", Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")){
                new MyLogger("Ziskanie pouzivatelov z databazy prebehlo uspesne ","INFO");
                return response.get("users");
            }else{
                new MyLogger("Chyba pri ziskavani pouzivatelov z databazy","ERROR");
                return null;
            }
        });
    }

    public JsonNode loadArtist(int id, int what){
        val payload = new ObjectNode();
        String url;

        if (what == 0){
            payload.set("id", id);
            url = "loadArtistByUser";
        } else {
            payload.set("id", what);
            url = "loadArtistByArtist";
        }

        return queryServerJson(url, Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")){
                new MyLogger("Ziskanie interpreta z databazy prebehlo uspesne ","INFO");
                return response.get("artist");
            }else{
                new MyLogger("Chyba pri ziskavani interpreta z databazy","ERROR");
                return null;
            }
        });
    }

    public JsonNode getArtistsByRegex(String regex){
        val payload = new ObjectNode();
        String url;
        payload.set("regex", regex);
        url = "loadArtistsByRegex";
        return queryServerJson(url, Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")){
                new MyLogger("Ziskanie interpretov z databazy prebehlo uspesne ","INFO");
                return response.get("artists");
            }else{
                new MyLogger("Chyba pri ziskavani interpretov z databazy","ERROR");
                return null;
            }
        });
    }

    public JsonNode loadAlbumsRegex(String regex){
        val payload = new ObjectNode();
        String url;
        payload.set("regex", regex);
        url = "loadAlbumsByRegex";

        return queryServerJson(url, Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")){
                new MyLogger("Ziskanie albumov z databazy prebehlo uspesne ","INFO");
                return response.get("albums");
            }else{
                new MyLogger("Chyba pri ziskavani albumov z databazy","ERROR");
                return null;
            }
        });
    }

    public JsonNode authenticate(String email, String password) {
        val payload = new ObjectNode();
        payload.set("email", email);
        payload.set("password", password);
        return queryServerJson("auth", Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")) {
                new MyLogger("Uspesna autentifikacia pouzivatela","INFO");
                return response.get("user");
            } else {
                new MyLogger("Chyba pri autentifikacii pouzivatela "+email,"ERROR");
                return null;
            }
        });
    }

    public JsonNode getUserPlaylists(int id){
        val payload = new ObjectNode();
        payload.set("id", id);
        return queryServerJson("playlists", Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")) {
                new MyLogger("Ziskanie playlistov pouzivatela "+id+" z databazy prebehlo uspesne","INFO");
                return response.get("playlists");
            } else {
                new MyLogger("Chyba pri ziskavani playlistov pouzivatela "+id,"ERROR");
                return null;
            }
        });
    }

    public JsonNode getPublicPlaylists(){
        val payload = new ObjectNode();
        return queryServerJson("allplaylists", Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")) {
                new MyLogger("Ziskanie playlistov z databazy prebehlo uspesne","INFO");
                return response.get("playlists");
            } else {
                new MyLogger("Chyba pri ziskavani playlistov","ERROR");
                return null;
            }
        });
    }

    public JsonNode getGenres(){
        val payload = new ObjectNode();
        return queryServerJson("genres", Method.GET, payload, (code, response) -> {
            if (response.getString("status").equals("ok")) {
                new MyLogger("Ziskanie zanrov z databazy prebehlo uspesne","INFO");
                return response.get("genres");
            } else {
                new MyLogger("Chyba pri ziskavani zanrov","ERROR");
                return null;
            }
        });
    }

    public JsonNode getRequests(){
        val payload = new ObjectNode();
        return queryServerJson("getRequests", Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")) {
                new MyLogger("Ziskanie artist requestov prebehlo uspesne", "INFO");
                return response.get("requests");
            } else {
                new MyLogger("Chyba pri ziskavani requestov", "ERROR");
                return null;
            }
        });
    }

    public JsonNode getArtistsByGenre(int genre_id){
        if(genre_id < 0)
            return null;
        val payload = new ObjectNode();
        payload.set("genre_id",genre_id);
        return queryServerJson("artistsByGenre", Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")) {
                System.out.println(response.get("artists"));
                new MyLogger("Ziskanie interpretov podla zadaneho zanru "+genre_id+" prebehlo uspesne","INFO");
                return response.get("artists");
            } else {
                new MyLogger("Chyba pri ziskavani interpretov podla zanru "+genre_id,"ERROR");
                return null;
            }
        });
    }

    //Downloads a song from the server, and stores it in the local file storage.
    public boolean downloadSongData(int songID) {
        val payload = new ObjectNode();
        payload.set("song_id", songID);
        try {
            return queryServerRaw("downloadSongData", Method.POST, payload, (code, response) -> {
                if (code != 200) return false;
                val file = IOUtil.getSongFile(songID);
                try {
                    IOUtil.streamToFile(response, file);
                    new MyLogger("Stahovanie skladby "+songID+" prebehlo uspesne","INFO");
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            new MyLogger("Chyba pri stahovani skladby "+songID,"ERROR");
            return false;
        }
    }

    //Uploads a song file to the server.
    public boolean uploadSongData(File songFile, int songID) throws IOException {
        val fileSize = Files.size(songFile.toPath());
        if (fileSize > 0xFFFFFFFFL) throw new IllegalArgumentException("File " + songFile + " is too big to upload!");

        val prefixStream = new ByteArrayOutputStream();
        IOUtil.writeInt(songID, prefixStream);
        IOUtil.writeInt((int)fileSize, prefixStream);
        val prefix = prefixStream.toByteArray();
        val prefixInput = new ByteArrayInputStream(prefix);
        try (val fileInput = new BufferedInputStream(new FileInputStream(songFile)); val sequence = new SequenceInputStream(prefixInput, fileInput)) {
            new MyLogger("Skladba "+songID+" bola uspesne nahrana na server","INFO");
            return queryServerJson("uploadSongData", Method.POST, fileSize + prefix.length, sequence, (code, response) -> response.getString("status").equals("ok"));
        }
    }

    public int createSong(String name, int artistId, int albumId, int genreId, int duration){
        val payload = new ObjectNode();
        payload.set("name", name);
        payload.set("artist_id", artistId);
        payload.set("album_id", albumId);
        payload.set("genre_id", genreId);
        payload.set("duration", duration);
        return queryServerJson("lastSongId", Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")) {
                new MyLogger("Ziskanie id poslednej pesnicky prebehlo uspesne","INFO");
                return response.getInt("id");
            } else {
                new MyLogger("Chyba pri ziskavani id poslednej pesnicky","ERROR");
                return 0;
            }
        });
    }

    public JsonNode songsByPlaylist(int playlist_id){
        if(playlist_id < 0)
            return null;
        val payload = new ObjectNode();
        payload.set("playlist_id",playlist_id);
        return queryServerJson("songsByPlaylist", Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")) {
                System.out.println(response.get("songs"));
                new MyLogger("Ziskavanie skladieb v playliste "+playlist_id+"prebehlo uspesne","INFO");
                return response.get("songs");
            } else {
                new MyLogger("Chyba pri ziskavani skladieb v playliste "+playlist_id,"ERROR");
                return null;
            }
        });
    }

    public JsonNode songsByAlbum(int album_id){
        if(album_id < 0)
            return null;
        val payload = new ObjectNode();
        payload.set("album_id",album_id);
        return queryServerJson("songsByAlbum", Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")) {
                System.out.println(response.get("songs"));
                new MyLogger("Ziskavanie skladieb v albume "+album_id+"prebehlo uspesne","INFO");
                return response.get("songs");
            } else {
                new MyLogger("Chyba pri ziskavani skladieb v albume "+album_id,"ERROR");
                return null;
            }
        });
    }

    //TODO server
    public JsonNode songsByRegex(String regex){
        val payload = new ObjectNode();
        payload.set("regex",regex);
        return queryServerJson("songsByRegex", Method.POST, payload, (code, response) -> {
            if (response.getString("status").equals("ok")) {
                System.out.println(response.get("songs"));
                new MyLogger("Ziskavanie skladieb prebehlo uspesne","INFO");
                return response.get("songs");
            } else {
                new MyLogger("Chyba pri ziskavani skladieb ","ERROR");
                return null;
            }
        });
    }

    public boolean deleteSong(int songId, int collectionId, int what){
        new MyLogger("Pokus o zmazanie skladby "+songId,"WARNING");
        val payload = new ObjectNode();
        payload.set("collection", what);
        payload.set("collection_id", collectionId);
        payload.set("song_id", songId);
        return queryServerJson("deleteSong", Method.POST, payload, (code, response) -> {
            if(response.getString("status").equals("ok")){
                new MyLogger("Skladba "+songId+" bola zmazana úspešne","INFO");
                return true;
            }
            else {
                new MyLogger("Skladbu "+songId+" sa nepodarilo odstrániť","ERROR");
                return false;
            }
        });
    }

    public boolean deleteCollection(int id, int what){
        new MyLogger("Pokus o zmazanie kolekcie "+id,"WARNING");
        val payload = new ObjectNode();
        payload.set("collection", what);
        payload.set("collection_id", id);
        return queryServerJson("deleteCollection", Method.POST, payload, (code, response) ->{
                if(response.getString("status").equals("ok")){
                    new MyLogger("Kolekcia "+id+" bola zmazana úspešne","INFO");
                    return true;
                }
                else {
                    new MyLogger("Kolekciu "+id+" sa nepodarilo odstrániť","ERROR");
                    return false;
                }
        });

    }

    public boolean addToPlaylist(int playlistId, int songId){
        new MyLogger("Pokus o pridanie skladby "+songId+" do playlistu "+playlistId,"WARNING");
        val payload = new ObjectNode();
        payload.set("song_id", songId);
        payload.set("collection_id", playlistId);
        return queryServerJson("addToPlaylist", Method.POST, payload, (code, response) ->{
            if(response.getString("status").equals("ok")){
                new MyLogger("Skladba "+songId+" bola úspešne pridaná do playlistu "+playlistId,"INFO");
                return true;
            }
            else {
                new MyLogger("Skladbu "+songId+" sa nepodarilo pridať do playlistu "+playlistId,"ERROR");
                return false;
            }
        });
    }

    public boolean updateUser(int userId, int statusId, int process){
        val payload = new ObjectNode();
        payload.set("user_id", userId);
        payload.set("status_id", statusId);
        payload.set("process", process);
        return queryServerJson("updateUser", Method.POST, payload, (code, response) ->
                response.getString("status").equals("ok"));
    }
    public int updateUser(int userId, String newPswd, String nickName, String oldPswd){
        val payload = new ObjectNode();
        payload.set("user_id", userId);
        payload.set("new_password", newPswd);
        payload.set("nickname", nickName);
        payload.set("old_password", oldPswd);
        return queryServerJson("updateUserSettings", Method.POST, payload, (code, response) ->
                response.getInt("valid"));
    }

    public boolean createCollection(int id, String name, String description, int what){
        new MyLogger("Pokus o vytvorenie kolekcie "+name,"WARNING");
        val payload = new ObjectNode();
        payload.set("collection", what);
        payload.set("id", id);
        payload.set("name", name);
        payload.set("description", description);
        return queryServerJson("createCollection", Method.POST, payload, (code, response) ->{
            if(response.getString("status").equals("ok")){
                new MyLogger("Kolekcia "+name+" bola úspešne vytvorená s ID: "+id,"INFO");
                return true;
            }
            else {
                new MyLogger("Kolekciu "+name+" sa nepodarilo vytvoriť","ERROR");
                return false;
            }
        });
    }

    public boolean editArtist(int id, String nameField, String descriptionField, String genre){
        val payload = new ObjectNode();
        payload.set("id", id);
        payload.set("name", nameField);
        payload.set("description", descriptionField);
        payload.set("genre", genre);
        System.out.println(id + nameField + descriptionField + genre);
        return queryServerJson("editArtist", Method.POST, payload,
                (code, response) -> response.getString("status").equals("ok"));
    }

    public boolean createArtist(String user_email){
        val payload = new ObjectNode();
        payload.set("user_email", user_email);
        return queryServerJson("create_artist", Method.POST, payload, (code, response) -> response.getString("status").equals("ok"));
    }

    //Internal communication code below

    /**
     * queryServerJson nam sluzi ako prepojenie na endpoint na API a vrati nam response zo servera
     * @param url je url pre HTTP request na rozlisenie endpointu na ktory sa chceme dopytovat
     * @param method HTTP request metoda
     * @param request payload s datami, ktore potrebujeme do sql
     * @param callback response s datami, ktore dopytujeme v tavre JSONNode
     * @return
     * @param <R>
     */
    private <R> R queryServerJson(String url, Method method, JsonNode request, ServerQueryCallback<JsonNode, R> callback) {
        val raw = request.toString().getBytes(StandardCharsets.UTF_8);
        val in = new ByteArrayInputStream(raw);
        return queryServerJson(url, method, raw.length, in, callback);
    }

    private <R> R queryServerJson(String url, Method method, long payloadSize, InputStream payload, ServerQueryCallback<JsonNode, R> callback) {
        try {
            return queryServerBinary(url, method, payloadSize, payload, (code, bin) -> {
                JsonNode json;
                if (bin.length == 0) {
                    json = new ObjectNode();
                    json.set("status", "client_error-no_data");
                    json.set("details", "The server did not send any JSON data in the response.");
                } else {
                    try {
                        json = JsonNode.parse(new String(bin));
                    } catch (Exception e) {
                        e.printStackTrace();
                        json = new ObjectNode();
                        json.set("status", "client_error-corrupted_data");
                        json.set("details", "The server sent data that could not be parsed as JSON.");
                    }
                }
                return callback.processResponse(code, json);
            });
        } catch (IOException e) {
            val fakeResponse = new ObjectNode();
            fakeResponse.set("status", "client_error-query_failed");
            fakeResponse.set("details", "Error while querying server. This response was created by the client as a fallback.");
            return callback.processResponse(0, fakeResponse);
        }
    }

    private <R> R queryServerBinary(String url, Method method, JsonNode request, ServerQueryCallback<byte[], R> callback) throws IOException {
        val raw = request.toString().getBytes(StandardCharsets.UTF_8);
        val in = new ByteArrayInputStream(raw);
        return queryServerBinary(url, method, raw.length, in, callback);
    }

    private <R> R queryServerBinary(String url, Method method, long payloadSize, InputStream payload, ServerQueryCallback<byte[], R> callback) throws IOException {
        return queryServerRaw(url, method, payloadSize, payload, (code, is) -> {
            var data = new byte[0];
            if (is != null)
                try {
                    data = is.readAllBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return callback.processResponse(code, data);
        });
    }

    private <R> R queryServerRaw(String url, Method method, JsonNode request, ServerQueryCallback<InputStream, R> callback) throws IOException {
        val raw = request.toString().getBytes(StandardCharsets.UTF_8);
        val in = new ByteArrayInputStream(raw);
        return queryServerRaw(url, method, raw.length, in, callback);
    }

    private <R> R queryServerRaw(String url, Method method, long payloadSize, InputStream payload, ServerQueryCallback<InputStream, R> callback) throws IOException {
        val requestURI = String.format("%s:%d/%s", hostname, port, url);
        val conn = (HttpURLConnection) new URL(requestURI).openConnection();
        conn.setRequestProperty("User-Agent", "musicverse-client");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod(method.name());
        conn.connect();
        val os = conn.getOutputStream();
        IOUtil.pipe(payload, os, payloadSize);
        os.flush();
        os.close();
        val responseCode = conn.getResponseCode();
        InputStream is;
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            is = null;
        }
        return callback.processResponse(responseCode, is);
    }

    public interface ServerQueryCallback<T, R> {
        R processResponse(int responseCode, T responseBody);
    }

    public enum Method {
        GET, POST
    }

}
