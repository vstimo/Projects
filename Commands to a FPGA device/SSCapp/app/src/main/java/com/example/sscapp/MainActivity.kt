package com.example.sscapp
import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothManager: BluetoothManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Create an instance of BluetoothManager
            bluetoothManager = BluetoothManager(context = this)
            // Initialize Bluetooth
            bluetoothManager.initializeBluetooth()
            // Set content to your Compose UI
            SSCApp(bluetoothManager)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        // Clean up Bluetooth resources when the activity is destroyed
        bluetoothManager.onDestroy()
    }
}
@Composable
fun ShadowedText(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black, // Change the text color to black
            modifier = Modifier
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun WelcomeScreen(navController: NavHostController, bluetoothManager:BluetoothManager) {
    var pairedDevicesList by remember { mutableStateOf<Set<BluetoothDevice>?>(null) }
    var isListVisible by remember { mutableStateOf(false) }
    var isListVisible1 by remember { mutableStateOf(false) }
    var isListVisible2 by remember { mutableStateOf(false) }
    var isListVisibleButon by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(5000) // Așteaptă 5 secunde înainte de a afișa primul mesaj
        isListVisible = true
        pairedDevicesList = setOf() // Poți seta aici lista de dispozitive Bluetooth, dacă este cazul
    }

    Column {
        ShadowedText("Welcome")

        // Sliding message: START TO CONTROL FPGA LEDS
        LaunchedEffect(isListVisible) {
            if (isListVisible) {
                isListVisible1 = true
                delay(5000)
            }
        }
        AnimatedVisibility(
            visible = isListVisible,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            Text(
                text = "START TO CONTROL FPGA LEDS",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Left
            )
        }

        LaunchedEffect(isListVisible1) {
            if (isListVisible1) {
                delay(5000)
                isListVisible2 = true;
            }
        }
        AnimatedVisibility(
            visible = isListVisible1,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "STEP1: ENSURE TO CONNECT TO THE BLUETOOTH MODULE",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp), // Adaugă spațiu între cele două texte
                    textAlign = TextAlign.Left
                )
                Row() {
                    Text(text = "Press Connect",
                        fontSize = 18.sp,
                        color = Color.Blue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp), // Adaugă spațiu între cele două texte
                        textAlign = TextAlign.Right
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isListVisible2,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "STEP2: GO TO LEDS' SCREEN",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp), // Adaugă spațiu între cele două texte
                    textAlign = TextAlign.Left
                )
                Row() {
                    Text(text = "Press Ready",
                        fontSize = 18.sp,
                        color = Color.Blue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp), // Adaugă spațiu între cele două texte
                        textAlign = TextAlign.Right
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center // Modificat pentru a centra textul în interiorul rândului
            ) {
                Text(
                    text = "If the connection failed, try again",
                    color = Color.Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth() // Adăugat pentru a limita lățimea la conținutul textului
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Hint: make sure you have the phone paired with the bluetooth module",
                    color = Color.Blue,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Button to show the list of paired devices
                Button(
                    onClick = {
                        pairedDevicesList = bluetoothManager.getPairedDevices()
                        isListVisibleButon = true
                    }
                ) {
                    Text("Show Paired Devices")
                }
                Spacer(modifier = Modifier.width(8.dp)) // Spațiu gol între butoane
                Button(
                    onClick = {
                        bluetoothManager.connectToDevice("68:27:19:F9:0A:07")
                    },
                    modifier = Modifier
                        .weight(2f)
                ) {
                    Text("Connect")
                }
            }
            // Hidden list of paired devices
            if (isListVisibleButon) {
                Column {
                    Text("Paired Devices:")
                    pairedDevicesList?.forEach { device ->
                        if (ActivityCompat.checkSelfPermission(
                                LocalContext.current,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Request Bluetooth permission
                            return
                        }
                        Text("Name: ${device.name}, Address: ${device.address}")
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate("leds")
                    },
                    modifier = Modifier
                        .fillMaxWidth() // Acesta face ca butonul să ocupe tot spațiul disponibil pe orizontală
                ) {
                    Text("Ready")
                }

            }
        }
    }
}
@Composable
fun LedsScreen(bluetoothManager:BluetoothManager) {
    var fromBoard: Int = 0
    val (isClicked0, setIsClicked0) = remember { mutableStateOf(false) }
    val (isClicked1, setIsClicked1) = remember { mutableStateOf(false) }
    val (isClicked2, setIsClicked2) = remember { mutableStateOf(false) }
    val (isClicked3, setIsClicked3) = remember { mutableStateOf(false) }
    val (isClicked4, setIsClicked4) = remember { mutableStateOf(false) }
    val (isClicked5, setIsClicked5) = remember { mutableStateOf(false) }
    val (isClicked6, setIsClicked6) = remember { mutableStateOf(false) }
    val (isClicked7, setIsClicked7) = remember { mutableStateOf(false) }
    val (isClicked8, setIsClicked8) = remember { mutableStateOf(false) }
    val (isClicked9, setIsClicked9) = remember { mutableStateOf(false) }
    val (isClicked10, setIsClicked10) = remember { mutableStateOf(false) }
    val (isClicked11, setIsClicked11) = remember { mutableStateOf(false) }
    val (isClicked12, setIsClicked12) = remember { mutableStateOf(false) }
    val (isClicked13, setIsClicked13) = remember { mutableStateOf(false) }
    val (isClicked14, setIsClicked14) = remember { mutableStateOf(false) }
    val (isClicked15, setIsClicked15) = remember { mutableStateOf(false) }
    val (isClickedAllLeds, setIsClickedAllLeds) = remember { mutableStateOf(false) }
    val (isClickedAnimation, setIsClickedAnimation) = remember { mutableStateOf(false) }
    val (isClicked, setIsClicked) = remember { mutableStateOf(false) }

    var isClickedRGB by remember { mutableStateOf(false) }
    val buttonColor0 = if (isClicked0) Color.Green else Color.Gray
    val buttonColor1 = if (isClicked1) Color.Green else Color.Gray
    val buttonColor2 = if (isClicked2) Color.Green else Color.Gray
    val buttonColor3 = if (isClicked3) Color.Green else Color.Gray
    val buttonColor4 = if (isClicked4) Color.Green else Color.Gray
    val buttonColor5 = if (isClicked5) Color.Green else Color.Gray
    val buttonColor6 = if (isClicked6) Color.Green else Color.Gray
    val buttonColor7 = if (isClicked7) Color.Green else Color.Gray
    val buttonColor8 = if (isClicked8) Color.Green else Color.Gray
    val buttonColor9 = if (isClicked9) Color.Green else Color.Gray
    val buttonColor10 = if (isClicked10) Color.Green else Color.Gray
    val buttonColor11 = if (isClicked11) Color.Green else Color.Gray
    val buttonColor12 = if (isClicked12) Color.Green else Color.Gray
    val buttonColor13 = if (isClicked13) Color.Green else Color.Gray
    val buttonColor14 = if (isClicked14) Color.Green else Color.Gray
    val buttonColor15 = if (isClicked15) Color.Green else Color.Gray
    val buttonColor = if (isClicked) Color.Green else Color.Gray
    val buttonColorAllLeds = if (isClickedAllLeds) Color.Green else Color.Gray
    val buttonColorAnimation = if (isClickedAnimation) Color.Green else Color.Gray
    val buttonColorRGB = if (isClickedRGB) Color.Green else Color.Gray

    val asciiAnimation = if (buttonColorAnimation == Color.Gray) 255 else 0
    val asciiRGB = if (buttonColorRGB == Color.Gray) 1 else 2
    val asciiAllLeds = if (buttonColorAllLeds == Color.Gray) 3 else 4
    val ascii0 = if (buttonColor0 == Color.Gray) 5 else 6
    val ascii1 = if (buttonColor1 == Color.Gray) 7 else 8
    val ascii2 = if (buttonColor2 == Color.Gray) 9 else 10
    val ascii3 = if (buttonColor3 == Color.Gray) 11 else 12
    val ascii4 = if (buttonColor4 == Color.Gray) 13 else 14
    val ascii5 = if (buttonColor5 == Color.Gray) 15 else 16
    val ascii6 = if (buttonColor6 == Color.Gray) 17 else 18
    val ascii7 = if (buttonColor7 == Color.Gray) 19 else 20
    val ascii8 = if (buttonColor8 == Color.Gray) 21 else 22
    val ascii9 = if (buttonColor9 == Color.Gray) 23 else 24
    val ascii10 = if (buttonColor10 == Color.Gray) 25 else 26
    val ascii11 = if (buttonColor11 == Color.Gray) 27 else 28
    val ascii12 = if (buttonColor12 == Color.Gray) 29 else 30
    val ascii13 = if (buttonColor13 == Color.Gray) 31 else 32
    val ascii14 = if (buttonColor14 == Color.Gray) 33 else 34
    val ascii15 = if (buttonColor15 == Color.Gray) 35 else 36
    Column {
        Column{
            Row( horizontalArrangement = Arrangement.spacedBy(16.dp)){
                Text(text="Press any button and look at the basys3 board",
                    fontSize = 18.sp,
                    color=Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor0, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii0)
                            setIsClicked0(!isClicked0)
                        }
                )
                Text(text = "Led 0")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor1, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii1)
                            setIsClicked1(!isClicked1)
                        }
                )
                Text(text = "Led 1")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor2, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii2)
                            setIsClicked2(!isClicked2)
                        }
                )
                Text(text = "Led 2")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor3, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii3)
                            setIsClicked3(!isClicked3)
                        }
                )
                Text(text = "Led 3")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor4, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii4)
                            setIsClicked4(!isClicked4)
                        }
                )
                Text(text = "Led 4")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor5, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii5)
                            setIsClicked5(!isClicked5)
                        }
                )
                Text(text = "Led 5")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor6, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii6)
                            setIsClicked6(!isClicked6)
                        }
                )
                Text(text = "Led 6")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor7, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii7)
                            setIsClicked7(!isClicked7)
                        }
                )
                Text(text = "Led 7")
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor8, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii8)
                            setIsClicked8(!isClicked8)
                        }
                )
                Text(text = "Led 8")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor9, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii9)
                            setIsClicked9(!isClicked9)
                        }
                )
                Text(text = "Led 9")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor10, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii10)
                            setIsClicked10(!isClicked10)
                        }
                )
                Text(text = "Led 10")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor11, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii11)
                            setIsClicked11(!isClicked11)
                        }
                )
                Text(text = "Led 11")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor12, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii12)
                            setIsClicked12(!isClicked12)
                        }
                )
                Text(text = "Led 12")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor13, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii13)
                            setIsClicked13(!isClicked13)
                        }
                )
                Text(text = "Led 13")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor14, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii14)
                            setIsClicked14(!isClicked14)
                        }
                )
                Text(text = "Led 14")
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(35.dp) // Adjust the size of the circle
                        .background(buttonColor15, shape = CircleShape)
                        .clickable {
                            bluetoothManager.sendData(ascii15)
                            setIsClicked15(!isClicked15)
                        }
                )
                Text(text = "Led 15")
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 50.dp) // Adjust the size of the rectangle
                        .background(buttonColorAllLeds, shape = RoundedCornerShape(16.dp))
                        .clickable {
                            bluetoothManager.sendData(asciiAllLeds)
                            setIsClickedAllLeds(!isClickedAllLeds)
                        }
                )
                Text(text = "All leds", textAlign = TextAlign.Center,modifier = Modifier
                    .wrapContentWidth())
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 50.dp) // Adjust the size of the rectangle
                        .background(buttonColorAnimation, shape = RoundedCornerShape(16.dp))
                        .clickable {
                            bluetoothManager.sendData(asciiAnimation)
                            setIsClickedAnimation(!isClickedAnimation)
                        }
                )
                Text(text = "Animation", textAlign = TextAlign.Center,modifier = Modifier
                    .wrapContentWidth())
            }
            Column {
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 50.dp)
                        .background(buttonColorRGB, shape = RoundedCornerShape(16.dp))
                        .clickable {
                            isClickedRGB = !isClickedRGB
                            bluetoothManager.sendData(asciiRGB)
                        }
                )
                Text(text = "RGB", textAlign = TextAlign.Center,modifier = Modifier
                    .wrapContentWidth())
            }
        }
            Spacer(modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(11.dp)){
                Text(text="Press to get data from board",fontSize = 18.sp,
                    color=Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center)
            }
        Row(horizontalArrangement = Arrangement.spacedBy(11.dp)) {
            Column {
                Box(
                    modifier = Modifier
                        .size(width = 420.dp, height = 50.dp)
                        .background(buttonColor, shape = RoundedCornerShape(16.dp))
                        .clickable {
                            setIsClicked(!isClicked) // Invert the isClicked value on each click
                        }
                )
                if (isClicked) {
                    fromBoard = bluetoothManager.receiveData()!!
                    Text(text = fromBoard.toString(),
                        color=Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center)
                }
            }
        }
        }
    }
}
    @Composable
    fun SSCApp(bluetoothManager: BluetoothManager) {
        // Create a NavHostController
        val navController = rememberNavController()
        // Set up the navigation graph
        NavHost(
            navController = navController,
            startDestination = "welcome"
        ) {
            composable("welcome") {
                Image(
                    painter = painterResource(id = R.drawable.bluetooth), // Înlocuiți cu resursa reală
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                WelcomeScreen(navController, bluetoothManager)
            }
            composable("leds") {
                Image(
                    painter = painterResource(id = R.drawable.led), // Înlocuiți cu resursa reală
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                LedsScreen(bluetoothManager)
            }
        }
    }



