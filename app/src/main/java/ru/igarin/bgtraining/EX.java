package ru.igarin.bgtraining;

public enum EX {
    LINGVISTICHESKIE_PIRAMIDY(R.string.lingvisticheskie_piramidy,
            "lingvisticheskie_piramidy.html", R.string.next_word),

    CHEM_VORON_POKHOZH_NA_STOL_1(
            R.string.chem_voron_pokhozh_na_stol_1,
            "chem_voron_pokhozh_na_stol_1.html", R.string.next_pair),

    CHEM_VORON_POKHOZH_NA_STOL_2(
            R.string.chem_voron_pokhozh_na_stol_2,
            "chem_voron_pokhozh_na_stol_2.html", R.string.next_pair),

    PRODVINUTOE_SVYAZYVANIE(
            R.string.prodvinutoe_svyazyvanie, "prodvinutoe_svyazyvanie.html",
            R.string.next_pair),

    O_CHEM_VIZHU_O_TOM_I_POYU(
            R.string.o_chem_vizhu_o_tom_i_poyu, "o_chem_vizhu_o_tom_i_poyu.html", R.string.next_word),

    ORANZHEVOE_NASTROENIE(
            R.string.oranzhevoe_nastroenie, "oranzhevoe_nastroenie.html",
            R.string.next_word),

    ALTERNATIVNOE_OPISANIE_DEYSTVITELNOSTI(
            R.string.alternativnoe_opisanie_deystvitelnosti, "alternativnoe_opisanie_deystvitelnosti.html",
            R.string.next_word),

    DRUGIE_VARIANTY_SOKRASHCHENIY(
            R.string.drugie_varianty_sokrashcheniy, "drugie_varianty_sokrashcheniy.html", R.string.next_word),

    RESHENIE_PROBLEM(R.string.reshenie_problem, "reshenie_problem.html", R.string.next_problem);

    EX(int string, String rule_file, int button_text) {
        this.id = string;
        this.string = string;
        this.rule_file = rule_file;
        this.button_text = button_text;
    }

    public int id;
    public int string;
    public String rule_file;
    public int button_text;
}