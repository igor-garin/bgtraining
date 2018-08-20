package ru.igarin.bgtraining;

public enum WordType {
    v_noun(R.string.v_noun),
    v_animal(R.string.v_animal),
    emotion(R.string.emotion),
    process(R.string.process),
    abbreviation(R.string.abbreviation),
    problem(R.string.problem);

    WordType(int str) {
        this.str = str;
    }

    public int str;
}