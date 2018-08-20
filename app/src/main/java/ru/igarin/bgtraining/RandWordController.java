package ru.igarin.bgtraining;

import android.content.Context;

import java.util.Random;

import ru.igarin.bgtraining.db.DataWord;
import ru.igarin.bgtraining.utils.SharedPrefer;

public class RandWordController {

    private static Random randomGenerator = new Random();

    public static final void getNextWord(Context context, EX ex) {

        String value = "";

        switch (ex) {
            case LINGVISTICHESKIE_PIRAMIDY:
                value = getRandWordWithUpperCase(context, WordType.v_noun);
                break;
            case CHEM_VORON_POKHOZH_NA_STOL_1:
                value = getRandWordWithUpperCase(context, WordType.v_noun) + " - "
                        + getRandWordWithUpperCase(context, WordType.v_animal);
                break;
            case CHEM_VORON_POKHOZH_NA_STOL_2:
                value = getRandWordWithUpperCase(context, WordType.v_noun) + " - "
                        + getRandWordWithUpperCase(context, WordType.emotion);
                break;
            case PRODVINUTOE_SVYAZYVANIE:
                value = getRandWordWithUpperCase(context, WordType.v_noun) + " - "
                        + getRandWordWithUpperCase(context, WordType.v_noun);
                break;
            case O_CHEM_VIZHU_O_TOM_I_POYU:
                value = getRandWordWithUpperCase(context, WordType.v_noun);
                break;
            case ORANZHEVOE_NASTROENIE:
                value = getRandWordWithUpperCase(context, WordType.v_noun);
                break;
            case ALTERNATIVNOE_OPISANIE_DEYSTVITELNOSTI:
                value = getRandWordWithUpperCase(context, WordType.process);
                break;
            case DRUGIE_VARIANTY_SOKRASHCHENIY:
                value = getRandWord(context, WordType.abbreviation);
                break;
            case RESHENIE_PROBLEM:
                value = getRandWord(context, WordType.problem);
                break;
        }

        SharedPrefer.getInstance().putString(SharedPrefer.SP_WORD, value);

        return;
    }

    private static String getRandWord(Context context, WordType type) {
        long count = DataWord.getCount(type.name());
        if (count == 0) {
            return "";
        }

        int randomInt = randomGenerator.nextInt((int) count);
        DataWord data = DataWord.get(randomInt, type.name());
        data.usecount++;
        data.update();
        return Character.toUpperCase(data.word.charAt(0)) + data.word.substring(1);
    }

    private static String firstToUpperCase(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    private static String getRandWordWithUpperCase(Context context, WordType type) {
        return firstToUpperCase(getRandWord(context, type));
    }
}
