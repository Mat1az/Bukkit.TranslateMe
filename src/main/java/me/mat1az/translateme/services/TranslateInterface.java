package me.mat1az.translateme.services;

import me.mat1az.translateme.models.Color;
import me.mat1az.translateme.models.ColorSet;
import me.mat1az.translateme.models.Language;
import me.mat1az.translateme.models.UserColor;

import java.util.List;
import java.util.UUID;

public interface TranslateInterface {

    int setLanguage(UUID player, int language);

    int setUserColor(UUID player, UserColor uc);

    List<Color> getColors();

    Color getColor(int colorID);

    UserColor getUserColor(UUID player);

    String translate(String message);

    String getMessage(int id, UUID player);

    List<ColorSet> getColorSets();
    List<Language> getLanguages();
    ColorSet getColorSet(int id);

}
