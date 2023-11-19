const int Senzor = A0;            //pinul pentru senzorul care capteaza informatie
int clap2 = 0;                    //variabila care memoreaza daca am batut de 2 ori din palme
long detection_range_start = 0;   //variabile pentru depistarea duratelor dintre batai
long detection_range = 0;
boolean status_lights = false;    //starea curenta a ledului
//////////////////////////////////////////////////
const int buzzer = 11;            //pinul pentru buzzerul care difuzeaza sunetul
//////////////////////////////////////////////////
const int red = 7;                //pinul RED din ledul RGB
const int green = 6;              //pinul GREEN din ledul RGB
const int blue = 5;               //pinul BLUE din ledul RGB
int clap1 = 0;                    //variabila care memoreaza daca am batut o data din palme
long detection_range_start1 = 0;  //variabile pentru depistarea duratelor dintre batai
long detection_range1 = 0;
boolean status_lights1 = false;   //starea curenta a ledului RGB
//////////////////////////////////////////////////
const int button=4;               //pinul pentru buton de PLAY/STOP
const int iesire=9;               //pinul pentru iesire si intrare in UNO
int buttonState=0;                //citirea ce vine din pinul pentru citire
int iesireState=LOW;              //starea curenta pentru pinul de intrare
int lastIesireState=LOW;          //starea precedenta pentru pinul de intrare


void setup() {
  pinMode(Senzor, INPUT);  //A0
  pinMode(10, OUTPUT);

  pinMode(red, OUTPUT); //7
  pinMode(green, OUTPUT); //6
  pinMode(blue, OUTPUT); //5

  pinMode(button,INPUT); //4
  pinMode(iesire,OUTPUT); //9
  digitalWrite(iesire,iesireState);
}

void loop() {
  aprindere_stingere_leduri();
  aprindereRGB();
  apasare_buton_play_stop();
}

void apasare_buton_play_stop(){
  unsigned long lastDebounceTime = 0; //variabila care ajuta la depistarea duratei de apasare a butonului
  unsigned long debounceDelay = 50;
  int reading = digitalRead(button); //reading ia valoarea HIGH daca butonul este apasat, altfel LOW
  
  if(reading!=lastIesireState) lastDebounceTime = millis();
  //daca am apasat butonul masuram timpul pana ridicam degetul de pe acesta, iar daca timpul depaseste 50 de ms(debounceDelay) este considerat VALID
  if ((millis() - lastDebounceTime) > debounceDelay) {
     if (reading != buttonState) {
      buttonState = reading; //transmitem mai departe informatia citita
      
      if (buttonState == HIGH){
        iesireState = !iesireState;
        super_mario();
      }
  }
  }
  digitalWrite(iesire,iesireState); //pe iesire afisam starea acesteia
  lastIesireState=reading; //si memoram ultima stare
}

void turning_on(){
  //subprogram care produce un anumit sunet atunci cand se bate de 2 ori pentru a porni ledul
  tone(buzzer, 783);
  delay(250);
  tone(buzzer, 987);
  delay(250);
  tone(buzzer, 1174);
  delay(250);
  tone(buzzer, 1566);
  delay(600);
  noTone(buzzer);
}

void turning_off(){
  //subprogram care produce un anumit sunet atunci cand se bate de 2 ori pentru a stinge ledul
  tone(buzzer, 1566);
  delay(250);
  tone(buzzer, 1174);
  delay(250);
  tone(buzzer, 987);
  delay(250);
  tone(buzzer, 783);
  delay(600);
  noTone(buzzer);
}

void aprindereRGB() {
  if (digitalRead(Senzor) == 0) {
    if (clap1 == 0) {
      detection_range_start1 = detection_range1 = millis(); //daca nu am patut deloc din palme incepem detectia bataii
      clap1++; 
    } else if (clap1 > 0 && millis() - detection_range1 >= 50) {
      detection_range1 = millis();
      clap1++; //daca au trecut macar 50ms si am batut din palme atunci crestem numarul de batai
    }
  }
  if (millis() - detection_range_start1 >= 400) {
    if (clap1 == 1) { //in momentul cand avem o bataie nu ne mai intereseaza starea altor leduri, subprogramul alege random culorile si aprinde RGB-ul si stinge celalat led
        digitalWrite(10, LOW);
        analogWrite(red, random(0,255));
        analogWrite(green, random(0,255));
        analogWrite(blue, random(0,255));
        if(status_lights==true) turning_off();
        status_lights=false;          
         }
    clap1 = 0;
  }
}

void aprindere_stingere_leduri() {
  if (digitalRead(Senzor) == 0) {
    if (clap2 == 0) {
      detection_range_start = detection_range = millis(); //daca nu am patut deloc din palme incepem detectia bataii
      clap2++;
    } else if (clap2 > 0 && millis() - detection_range >= 50) {
      detection_range = millis();
      clap2++; //daca au trecut macar 50ms si am batut din palme atunci crestem numarul de batai
    }
  }
  if (millis() - detection_range_start >= 400) {
    if (clap2 == 2) { //in momentul in care am batut de 2 ori verificam daca ledul verde(cel controleaza practic sistem) este stins/aprins
        digitalWrite(red, LOW);
        digitalWrite(green, LOW);
        digitalWrite(blue, LOW);
      if (!status_lights) {
        status_lights = true; //daca este stins il aprindem la urmatoare bataie de 2 ori din palme si actualizam starea in 'APRINS'
        digitalWrite(10, HIGH);
        turning_on();
       } else if (status_lights) {
        status_lights = false; //daca este aprins il stingem la urmatoare bataie de 2 ori din palme si actualizam starea in 'STINS'
        digitalWrite(10, LOW);
        turning_off();
      }
    }
    clap2 = 0;
  }
}

void super_mario() {
  //Definim notele muzicale
  #define NOTE_E6 1319
  #define NOTE_G6 1568
  #define NOTE_A6 1760
  #define NOTE_AS6 1865
  #define NOTE_B6 1976
  #define NOTE_C7 2093
  #define NOTE_D7 2349
  #define NOTE_E7 2637
  #define NOTE_F7 2794
  #define NOTE_G7 3136
  #define NOTE_A7 3520

  //Linia melodica(partitura), unde este 0 inseamna ca este pauza
  int melody[] = {
    NOTE_E7, NOTE_E7, 0, NOTE_E7,
    0, NOTE_C7, NOTE_E7, 0,
    NOTE_G7, 0, 0, 0,
    NOTE_G6, 0, 0, 0,

    NOTE_C7, 0, 0, NOTE_G6,
    0, 0, NOTE_E6, 0,
    0, NOTE_A6, 0, NOTE_B6,
    0, NOTE_AS6, NOTE_A6, 0,

    NOTE_G6, NOTE_E7, NOTE_G7,
    NOTE_A7, 0, NOTE_F7, NOTE_G7,
    0, NOTE_E7, 0, NOTE_C7,
    NOTE_D7, NOTE_B6, 0, 0,

    NOTE_C7, 0, 0, NOTE_G6,
    0, 0, NOTE_E6, 0,
    0, NOTE_A6, 0, NOTE_B6,
    0, NOTE_AS6, NOTE_A6, 0,

    NOTE_G6, NOTE_E7, NOTE_G7,
    NOTE_A7, 0, NOTE_F7, NOTE_G7,
    0, NOTE_E7, 0, NOTE_C7,
    NOTE_D7, NOTE_B6, 0, 0
  };

  //Duratele pentru fiecare dintre notele muzica de mai sus
  int tempo[] = {
    12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,

    12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,

    9,9,9,12,12,12,12,12,12,12,12,12,12,12,12,

    12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,

    9,9,9,12,12,12,12,12,12,12,12,1212,12,12,
  };
  
  repetare1:
  for (int thisNote = 0; thisNote < sizeof(melody) / sizeof(int); thisNote++) {
    if(iesireState==LOW) break; //conditia pentru oprire din cantat
    apasare_buton_play_stop(); 
       
    //Pentru a calcula durata unei note muzica, luam o secunda si o divizam la durata notei
    //o patrime este 1000/4, o optime 1000/8, etc.

    int noteDuration = 1000 / tempo[thisNote];

    tone(buzzer, melody[thisNote], noteDuration);

    delay(noteDuration * 1.30);  //pauza intre note pentru a se putea diferentia acestea

    tone(buzzer, 0, noteDuration);
    if(thisNote==sizeof(melody)/sizeof(int)-1 && iesireState==HIGH) goto repetare1; //daca nu s-a apasat pe buton ne intoarcem si repetam melodia inca o data:)
  }
}