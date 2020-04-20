package com.example.thefactorgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    ArrayList<Integer> factors = new ArrayList<>();
    ArrayList<Integer> answers = new ArrayList<>();
    ProgressBar progressBar;
    Button option1;
    Button option2;
    Button option3;
    Button practiceFactors;
    Button playagain;
    TextView score;
    TextView timer;
    TextView highScore;
    CountDownTimer countDownTimer;
    ConstraintLayout constraintLayout;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    TextView intro;
    int maxScore;
    int scoreresult=0;
    int correctAnswerPosition;
    int correctFactor;
    int correctAnswer;
    public void getAnswers(View view){
        if(editText.getText().toString().equals("")) {
            Toast.makeText(this, "Enter a Number!", Toast.LENGTH_SHORT).show();
        }
        else {
            practiceFactors.setVisibility(View.INVISIBLE);

            int number = Integer.parseInt(editText.getText().toString());
            if (number <= 2) {
                Toast.makeText(this, "Enter number greater than 2", Toast.LENGTH_SHORT).show();
                practiceFactors.setVisibility(View.VISIBLE);
            } else {
                for (int i = 2; i < number; i++) {
                    if (number % i == 0) {
                        factors.add(i);
                    }
                }
                if (factors.size() == 0) {
                    Toast.makeText(this, "That's a prime number", Toast.LENGTH_SHORT).show();
                    practiceFactors.setVisibility(View.VISIBLE);
                } else {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    Random random = new Random();
                    correctAnswerPosition = random.nextInt(3);
                    correctFactor = random.nextInt(factors.size());
                    correctAnswer = factors.get(correctFactor);
                    for (int i = 0; i < 3; i++) {
                        if (i == correctAnswerPosition) {
                            answers.add(factors.get(correctFactor));
                        } else {
                            int wronganswer = random.nextInt(number + 1);
                            while (factors.contains(wronganswer) || answers.contains(wronganswer)||wronganswer==1||wronganswer==number) {
                                wronganswer = random.nextInt(number + 1);

                            }

                            answers.add(wronganswer);
                        }
                    }
                    option1.setText(Integer.toString(answers.get(0)));
                    option2.setText(Integer.toString(answers.get(1)));
                    option3.setText(Integer.toString(answers.get(2)));
                    option1.setVisibility(View.VISIBLE);
                    option2.setVisibility(View.VISIBLE);
                    option3.setVisibility(View.VISIBLE);
                    factors.clear();
                    answers.clear();
                    countDownTimer = new CountDownTimer(10100, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            timer.setText( "0:"+Long.toString(millisUntilFinished / 1000));
                            progressBar.setProgress((int)millisUntilFinished/100);
                        }

                        @Override
                        public void onFinish() {
                            option1.setVisibility(View.INVISIBLE);
                            option2.setVisibility(View.INVISIBLE);
                            option3.setVisibility(View.INVISIBLE);
                            practiceFactors.setVisibility(View.INVISIBLE);
                            editText.setVisibility(View.INVISIBLE);
                            updateMaxScore();
                            playagain.setText("Play Again");
                            playagain.setVisibility(View.VISIBLE);
                            highScore.setVisibility(View.VISIBLE);
                            progressBar.setProgress(0);
                            progressBar.setProgress(100);
                            timer.setText("0:10");
                        }
                    }.start();
                }
            }
        }
    }

    public void updateMaxScore(){
        maxScore=scoreresult;
        if(maxScore>sharedPreferences.getInt("maxScore",0)){
            sharedPreferences.edit().putInt("maxScore",maxScore).apply();
        }
        highScore.setText("Highscore:\n"+Integer.toString(sharedPreferences.getInt("maxScore",0)));
    }

    public void chooseAns(View view){
        countDownTimer.cancel();
        timer.setText("0:10");
        practiceFactors.setVisibility(View.VISIBLE);
        progressBar.setProgress(100);

        if(Integer.parseInt(view.getTag().toString())==correctAnswerPosition){
            Toast.makeText(this, "Correct Answer!!", Toast.LENGTH_SHORT).show();
            option1.setVisibility(View.INVISIBLE);
            option2.setVisibility(View.INVISIBLE);
            option3.setVisibility(View.INVISIBLE);
            editText.getText().clear();
            scoreresult++;
            score.setText("Score:\n"+Integer.toString(scoreresult));
            constraintLayout.setBackgroundColor(Color.parseColor("#00ff00"));
            new CountDownTimer(1100,1000){

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    int colorFrom = Color.parseColor("#00ff00");
                    int colorTo = Color.parseColor("#ffdde1");
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.setDuration(1000); // milliseconds
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            constraintLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });
                    colorAnimation.start();

                }
            }.start();
        }
        else{
            Toast.makeText(this, "Wrong!! Correct answer is " + Integer.toString(correctAnswer) , Toast.LENGTH_SHORT).show();
            option1.setVisibility(View.INVISIBLE);
            option2.setVisibility(View.INVISIBLE);
            option3.setVisibility(View.INVISIBLE);
            editText.getText().clear();
            score.setText("Score:\n"+Integer.toString(scoreresult));
            constraintLayout.setBackgroundColor(Color.RED);
            new CountDownTimer(1100,1000){

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    int colorFrom = Color.RED;
                    int colorTo = Color.parseColor("#ffdde1");
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.setDuration(1500); // milliseconds
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            constraintLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });
                    colorAnimation.start();
                }
            }.start();
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(200);
            }
        }

    }


    public void playAgain(View view){
        playagain.setVisibility(View.INVISIBLE);
        highScore.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        intro.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        editText.getText().clear();
        score.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        practiceFactors.setVisibility(View.VISIBLE);
        timer.setText("0:10");
        score.setText("Score:\n 0");
        scoreresult=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        score = findViewById(R.id.score);
        constraintLayout = findViewById(R.id.constraintLayout);
        timer = findViewById(R.id.timer);
        practiceFactors = findViewById(R.id.practiceFactorsButton);
        sharedPreferences = this.getSharedPreferences("com.example.thefactorgame", Context.MODE_PRIVATE);
        highScore = findViewById(R.id.highscore);
        playagain = findViewById(R.id.start);
        progressBar=findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);
        intro = findViewById(R.id.intro);
        progressBar.animate().rotation(270.0f);
        playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain(v);
            }
        });
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAns(v);
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAns(v);
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAns(v);
            }
        });
        practiceFactors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswers(v);
            }
        });
        constraintLayout.setBackgroundColor(Color.parseColor("#ffdde1"));
        updateMaxScore();












    }
}
