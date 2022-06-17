package com.musicverse.client.sessionManagement;

import com.musicverse.client.objects.User;
import lombok.SneakyThrows;

import java.awt.*;
import java.util.prefs.Preferences;

/**
 * funkcionalita Preferencies nam uchovava udaje o prihlasenom pouzivatelovi pomocou xml a tak su nam k dispozici
 * pocas celeho behu programu bez vytvarania novej instancie a teda implementuje pattern Singleton.
 * Ked zrusime program a sme prihlaseni, tak po opatovnom otvoreni programu budeme stale prihlaseni
 */
public class PreferencesLogin {

    private Preferences prefs;

    public void setPreference(User user) {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        String nickName = "nickname", email = "email", date = "date", id = "id", role = "role";
        prefs.put(nickName, user.getNickName());
        prefs.put(email, user.getEmail());
        prefs.put(date, user.getCreatedAt());
        prefs.putInt(id, user.getId());
        prefs.putInt(role, user.getRole());
    }

    public static User getPrefs() {
        Preferences prefs = Preferences.userRoot().node(PreferencesLogin.class.getName());
        return new User(prefs.get("nickname", "guest"), prefs.get("email", "none"),
                prefs.getInt("role", 0), prefs.getInt("id", -1), "xxxxxxxx");
    }


}
