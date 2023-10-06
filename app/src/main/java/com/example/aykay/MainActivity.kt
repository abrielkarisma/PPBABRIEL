package com.example.aykay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {


                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        Login(navController)
                    }
                    composable("homepage") {
                        HomePage(navController)
                    }
                    composable("register") {
                        Register(navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController) {
    val expanded = remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginEnabled by remember { mutableStateOf(false) }
    val Prim = Color(0xFF2D2424)
    val Sec = Color(0xFF5C3D2E)
    var jwt by remember { mutableStateOf("") }
    var baseUrl = "http://10.217.25.131:1337/api/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LoginService::class.java)
    val call = retrofit.getData(LoginData("abrielkarisma", "Abil2505?"))
    call.enqueue(object : Callback<LoginRespon> {
        override fun onResponse(call: Call<LoginRespon>, response: Response<LoginRespon>) {
            if (response.code() == 200) {
                jwt = response.body()?.jwt!!
//                print(jwt)
            }
        }


        override fun onFailure(call: Call<LoginRespon>, t: Throwable) {
            print(t.message)
        }

    })
//    val navController = rememberNavController()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ak),
            contentDescription = "akay",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(-20.dp)

        ) {

            Text(
                text = "Hai Aykayers",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                color = Prim,
                fontFamily = FontFamily(Font(R.font.aykay)),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Welcome Back !",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                color = Sec,
                fontFamily = FontFamily(Font(R.font.aykay)),
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Login",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                color = Prim,
                fontFamily = FontFamily(Font(R.font.aykay)),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = username,
                onValueChange = { newUsername: String ->
                    username = newUsername
                    isLoginEnabled = newUsername.isNotEmpty() && password.isNotEmpty()
                },
                label = { Text("Username") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { newPass: String ->
                    password = newPass
                    isLoginEnabled = username.isNotEmpty() && newPass.isNotEmpty()
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )

            OutlinedButton(
                onClick = { navController.navigate("homepage");expanded.value = !expanded.value },
                border = BorderStroke(2.dp, Sec)
            ) {
                Text(text = "Login", color = Sec)
            }
            Divider(modifier = Modifier.padding(16.dp))

            TextButton(
                onClick = { navController.navigate("Register");expanded.value = !expanded.value }
            ) {
                Text(
                    text = "Don't have an account yet? Register",
                    color = Color.Red,
                    textAlign = TextAlign.Right,
                )
            }
//            Text(text = jwt)
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun Register(navController: NavController) {
    val expanded = remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isLoginEnabled by remember { mutableStateOf(false) }
    val Prim = Color(0xFF2D2424)
    val Sec = Color(0xFF5C3D2E)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ak),
            contentDescription = "akay",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(-20.dp)

        ) {

            Text(
                text = "Hai Aykayers",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                color = Prim,
                fontFamily = FontFamily(Font(R.font.aykay)),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Welcome !",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                color = Sec,
                fontFamily = FontFamily(Font(R.font.aykay)),
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Register Here",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                color = Prim,
                fontFamily = FontFamily(Font(R.font.aykay)),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            OutlinedTextField(
                value = username,
                onValueChange = { newUsername: String ->
                    username = newUsername
                    isLoginEnabled = newUsername.isNotEmpty() && password.isNotEmpty()
                },
                label = { Text("Username") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { newPass: String ->
                    password = newPass
                    isLoginEnabled = username.isNotEmpty() && newPass.isNotEmpty()
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            OutlinedTextField(
                value = password,
                onValueChange = { newPass: String ->
                    password = newPass
                    isLoginEnabled = username.isNotEmpty() && newPass.isNotEmpty()
                },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )

            OutlinedButton(
                onClick = { navController.navigate("homepage");expanded.value = !expanded.value },
                border = BorderStroke(2.dp, Sec)
            ) {
                Text(text = "Register", color = Sec)
            }
            Divider(modifier = Modifier.padding(16.dp))

            TextButton(
                onClick = { navController.navigate("Login");expanded.value = !expanded.value }
            ) {
                Text(
                    text = "Already have an account? Login ",
                    color = Color.Red,
                    textAlign = TextAlign.Right,
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Login") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }, shape = CircleShape) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BottomAppBar {

                Text(
                    text = "Hoho..",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
        }
    }
}


