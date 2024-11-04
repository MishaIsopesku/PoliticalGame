package com.example.politicalgame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private TextView questionTextView, timerTextView, scoreTextView;
    private Button trueButton, falseButton, startButton;
    private RadioGroup modeRadioGroup;

    private List<String> ukrainianParties = Arrays.asList(
            "Слуга народу", "Європейська солідарність", "Батьківщина", "Опозиційна платформа – За життя",
            "Голос", "Радикальна партія Олега Ляшка", "Свобода", "Наш край", "Українська стратегія Гройсмана",
            "Народний фронт", "Самопоміч", "Партія Шарія", "Правий сектор", "Національний корпус", "УДАР"
    );

    private List<String> foreignParties = Arrays.asList(
            "Республіканська партія США", "Демократична партія США", "Консервативна партія Великобританії",
            "Лейбористська партія Великобританії", "Зелена партія Німеччини", "Альтернатива для Німеччини",
            "Французька соціалістична партія", "Республіканці Франції", "Ліга Італії", "Рух 5 зірок Італії",
            "Комунистична партія Китаю", "Ліберальна партія Канади", "Коаліція майбутнього Австралії",
            "Соціалістична партія Іспанії", "Національний фронт Франції"
    );

    private List<String> allParties = new ArrayList<>();
    private Set<String> askedParties = new HashSet<>();
    private int scoreCorrect = 0, scoreIncorrect = 0;
    private boolean isGameRunning = false;
    private boolean allowRepeats = false;

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.questionTextView);
        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);
        startButton = findViewById(R.id.startButton);
        modeRadioGroup = findViewById(R.id.modeRadioGroup);

        trueButton.setEnabled(false);
        falseButton.setEnabled(false);

        startButton.setOnClickListener(v -> startGame());

        trueButton.setOnClickListener(v -> checkAnswer(true));
        falseButton.setOnClickListener(v -> checkAnswer(false));
    }

    private void startGame() {
        scoreCorrect = 0;
        scoreIncorrect = 0;
        allParties.clear();
        allParties.addAll(ukrainianParties);
        allParties.addAll(foreignParties);
        Collections.shuffle(allParties);
        askedParties.clear();

        // Вибір режиму
        int selectedMode = modeRadioGroup.getCheckedRadioButtonId();
        allowRepeats = selectedMode == R.id.repeatModeButton;

        isGameRunning = true;
        startButton.setEnabled(false);
        trueButton.setEnabled(true);
        falseButton.setEnabled(true);

        nextQuestion();

        timer = new CountDownTimer(60000, 1000) {// Таймер на 1хв
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                endGame();
            }
        }.start();
    }

    private void nextQuestion() {
        if (!allowRepeats && askedParties.size() >= allParties.size()) {
            // Якщо всі питання задані (в режимі без повторень), гра завершується
            endGame();
            return;
        }

        String party;
        if (allowRepeats) {
            party = allParties.get((int) (Math.random() * allParties.size())); // Випадковий вибір із повтореннями
        } else {
            do {
                party = allParties.get((int) (Math.random() * allParties.size())); // Без повторень
            } while (askedParties.contains(party) && askedParties.size() < allParties.size());
            askedParties.add(party);
        }

        questionTextView.setText(party);
    }

    private void checkAnswer(boolean answer) {
        String currentParty = questionTextView.getText().toString();
        boolean isUkrainianParty = ukrainianParties.contains(currentParty);

        if ((isUkrainianParty && answer) || (!isUkrainianParty && !answer)) {
            scoreCorrect++;
            Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show();
        } else {
            scoreIncorrect++;
            Toast.makeText(this, "Неправильно!", Toast.LENGTH_SHORT).show();
        }

        nextQuestion();
    }

    private void endGame() {
        isGameRunning = false;
        startButton.setEnabled(true);
        trueButton.setEnabled(false);
        falseButton.setEnabled(false);

        if (timer != null) {
            timer.cancel();
        }

        timerTextView.setText("Час закінчився!");
        scoreTextView.setText("Вірно: " + scoreCorrect + " Невірно: " + scoreIncorrect);
    }
}


