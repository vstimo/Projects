#include "pitches.h"
#include <TimerOne.h>
#include <Stepper.h>
Stepper motor(2048, 7, 8, 9, 10);

#define SENSOR_PIN 5 //microfon
#define RELAY_PIN 6 //bec
#define BUZZER_PIN 11
#define RED_PIN 2
#define GREEN_PIN 3
#define BLUE_PIN 4
#define PHOTOREZISTOR_PIN A0

#define DELAY_BETWEEN_CLAPS 50  //timp de asteptare dupa bataie(previne detectia mai multor batai ca o bataie)
#define DIFF_MAX 400            //numar max de milisecunde intre 2 batai
#define HOW_BRIGHT 900

int motorSpeed = 10;
void setup() {
  pinMode(SENSOR_PIN, INPUT_PULLUP);
  pinMode(PHOTOREZISTOR_PIN, INPUT_PULLUP);
  pinMode(RELAY_PIN, OUTPUT);
  digitalWrite(RELAY_PIN, HIGH);
  pinMode(BUZZER_PIN, OUTPUT);
  pinMode(RED_PIN, OUTPUT);
  pinMode(GREEN_PIN, OUTPUT);
  pinMode(BLUE_PIN, OUTPUT);
  motor.setSpeed(motorSpeed);
  Serial.begin(9600);
}

int clap1 = 0, clap2 = 0;
long detection_range_start = 0, detection_range = 0, detection_range_start1 = 0, detection_range1 = 0;  //variabile pentru depistarea duratelor dintre batai
boolean status_bulb = false;

void loop() {
  automaticCurtains();
  attachInterrupt(digitalPinToInterrupt(RELAY_PIN), turn_on_off, CHANGE);
  aprindere_stingere_led();
  aprindere_RGB();
}

unsigned previousMillis = 0;
void myDelay(unsigned long ms) {
  unsigned long currentMillis = millis();
  while (currentMillis - previousMillis < ms) currentMillis = millis();

  previousMillis = currentMillis;
}

boolean day = false;
void automaticCurtains() {
  //Serial.println(analogRead(PHOTOREZISTOR_PIN));
  if (analogRead(PHOTOREZISTOR_PIN) <= HOW_BRIGHT) {
    if (day == true) {
      day = false;
      miscare(3, 2048);
    }
  } else if (day == false) {
    day = true;
    miscare(3, -2048);
  }
}

void miscare(int repetitii, int rotatie) {
  for (int i = 0; i < repetitii; i++) {
    motor.step(rotatie);
    myDelay(100);
  }
}

void aprindere_stingere_led() {
  if (digitalRead(SENSOR_PIN) == 0) {
    if (clap2 == 0) {  //prima bataie din palme
      detection_range_start = detection_range = millis();
      clap2++;
    } else if (clap2 > 0 && millis() - detection_range >= DELAY_BETWEEN_CLAPS) {  //a doua bataie din palme
      detection_range = millis();
      clap2++;  //daca au trecut macar 50ms si am batut din palme atunci crestem numarul de batai
    }
  }

  if (millis() - detection_range_start >= DIFF_MAX) {
    if (clap2 == 2) {  //in momentul in care am batut de 2 ori verificam daca ledul este stins/aprins
      if (!status_bulb) {
        digitalWrite(RELAY_PIN, LOW);
        status_bulb = true;
      } else if (status_bulb) {
        digitalWrite(RELAY_PIN, HIGH);
        status_bulb = false;
        analogWrite(RED_PIN, LOW);
        analogWrite(GREEN_PIN, LOW);
        analogWrite(BLUE_PIN, LOW);
      }
      turn_on_off();
    }
    clap2 = 0;
  }
}

void aprindere_RGB() {
  if (digitalRead(SENSOR_PIN) == 0) {
    if (clap1 == 0) {
      detection_range_start1 = detection_range1 = millis();  //daca nu am patut deloc din palme incepem detectia bataii
      clap1++;
    } else if (clap1 > 0 && millis() - detection_range1 >= 50) {
      detection_range1 = millis();
      clap1++;  //daca au trcecut macar 50ms si am batut din palme atunci se pot aprinde ledurile rgb
    }
  }
  if (millis() - detection_range_start1 >= DIFF_MAX) {
    if (clap1 == 1) {  //in momentul cand avem o bataie nu ne mai intereseaza starea altor leduri, subprogramul alege random culorile si aprinde RGB-ul si stinge celalat led
      digitalWrite(RELAY_PIN, HIGH);
      status_bulb = false;
      analogWrite(RED_PIN, random(0, 255));
      analogWrite(GREEN_PIN, random(0, 255));
      analogWrite(BLUE_PIN, random(0, 255));
    }
    clap1 = 0;
  }
}

boolean on = true;
void turn_on_off() {
  if (on) {
    myDelay(250);
    tone(BUZZER_PIN, NOTE_G5);
    myDelay(250);
    tone(BUZZER_PIN, NOTE_B5);
    myDelay(250);
    tone(BUZZER_PIN, NOTE_D6);
    myDelay(250);
    tone(BUZZER_PIN, NOTE_G6);
    myDelay(600);
    noTone(BUZZER_PIN);
  } else {
    myDelay(250);
    tone(BUZZER_PIN, NOTE_G6);
    myDelay(250);
    tone(BUZZER_PIN, NOTE_D6);
    myDelay(250);
    tone(BUZZER_PIN, NOTE_B5);
    myDelay(250);
    tone(BUZZER_PIN, NOTE_G5);
    myDelay(600);
    noTone(BUZZER_PIN);
  }
  on = !on;
}