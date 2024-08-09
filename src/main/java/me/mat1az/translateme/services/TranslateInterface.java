package me.mat1az.translateme.services;

import me.mat1az.translateme.models.*;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.UUID;

public interface TranslateInterface {

    int setLanguage(UUID player, int language);

    int setUserColor(UUID player, UserColor uc);

    List<Color> getColors();

    Color getColor(int colorID);

    UserColor getUserColor(UUID player);

    String colorize(String message);

    TextComponent getMessage(int id, UUID player, ReplaceHolder... repl);

    List<ColorSet> getColorSets();
    List<Language> getLanguages();
    ColorSet getColorSet(int id);

}
