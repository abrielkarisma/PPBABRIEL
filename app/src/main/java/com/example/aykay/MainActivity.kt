package com.example.aykay

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_app_jetpack_compose.response.UserRespon
import com.example.android_app_jetpack_compose.service.UserService
import com.example.compose.AppTheme
import com.example.helloandroid.PreferencesManager
import com.example.helloandroid.data.LoginData
import com.example.helloandroid.data.RegisterData
import com.example.helloandroid.respon.LoginRespon
import com.example.helloandroid.service.LoginService
import com.example.helloandroid.service.RegisterService
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


                val sharedPreferences: SharedPreferences =
                    LocalContext.current.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val navController = rememberNavController()

                var startDestination: String
                var jwt = sharedPreferences.getString("jwt", "")
                if (jwt.equals("")) {
                    startDestination = "login"
                } else {
                    startDestination = "homepage"
                }
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        Login(navController)
                    }
                    composable("homepage") {
                        Homepage(navController)
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
fun Login(navController: NavController, context: Context = LocalContext.current) {
    val preferencesManager = remember { PreferencesManager(context = context) }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var baseUrl = "http://10.0.2.2:1337/api/"
    var jwt by remember { mutableStateOf("") }
    val Prim = Color(0xFF2D2424)
    val Sec = Color(0xFF5C3D2E)

    jwt = preferencesManager.getData("jwt")

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
                onValueChange = { newText ->
                    username = newText
                },
                label = { Text("Username") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { newText ->
                    password = newText
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )

            OutlinedButton(
                onClick = {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(LoginService::class.java)
                    val call = retrofit.getData(LoginData(username.text, password.text))
                    call.enqueue(object : Callback<LoginRespon> {
                        override fun onResponse(
                            call: Call<LoginRespon>,
                            response: Response<LoginRespon>
                        ) {
                            print(response.code())
                            if (response.code() == 200) {
                                jwt = response.body()?.jwt!!
                                preferencesManager.saveData("jwt", jwt)
                                navController.navigate("homepage")
                            } else if (response.code() == 400) {
                                print("error login")
                                var toast = Toast.makeText(
                                    context,
                                    "Username atau password salah",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }

                        override fun onFailure(call: Call<LoginRespon>, t: Throwable) {
                            print(t.message)
                        }

                    })
                },

                border = BorderStroke(2.dp, Sec)
            ) {
                Text(text = "Login", color = Sec)
            }
            Divider(modifier = Modifier.padding(16.dp))

            TextButton(
                onClick = { navController.navigate("Register") }
            ) {
                Text(
                    text = "Don't have an account yet? Register",
                    color = Color.Red,
                    textAlign = TextAlign.Right,
                )
            }
            Text(text = jwt)
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun Register(navController: NavController, context: Context = LocalContext.current) {
    val preferencesManager = remember { PreferencesManager(context = context) }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
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
                value = email, onValueChange = { newText ->
                    email = newText
                }, label = { Text("Email") }
            )
            OutlinedTextField(value = username, onValueChange = { newText ->
                username = newText
            }, label = { Text("Username") })

            OutlinedTextField(
                value = password,
                onValueChange = { newText ->
                    password = newText
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            OutlinedButton(
                onClick = {
                    var baseUrl = "http://10.0.2.2:1337/api/"
                    val retrofit = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(RegisterService::class.java)
                    val call =
                        retrofit.saveData(RegisterData(email.text, username.text, password.text))
                    call.enqueue(object : Callback<LoginRespon> {
                        override fun onResponse(
                            call: Call<LoginRespon>,
                            response: Response<LoginRespon>
                        ) {
                            print(response.code())
                            if (response.code() == 200) {

                            } else if (response.code() == 400) {
                                print("error login")
                                var toast = Toast.makeText(
                                    context,
                                    "Username atau password salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginRespon>, t: Throwable) {
                            print(t.message)
                        }

                    })
                }) {
                Text(text = "Register", color = Sec)
            }
            OutlinedButton(onClick = {
                preferencesManager.saveData("jwt", "")
                navController.navigate("greeting")
            }) {
                Text("Logout")
            }
            Divider(modifier = Modifier.padding(16.dp))

            TextButton(
                onClick = { navController.navigate("Login") }
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
fun Homepage(navController: NavController, context: Context = LocalContext.current) {
    val listUser = remember { mutableStateListOf<UserRespon>() }

    val baseUrl = "http://10.0.2.2:1337/api/"
    val retrofit =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build().create(UserService::class.java)
    val call = retrofit.getData()
    call.enqueue(object : Callback<List<UserRespon>> {
        override fun onResponse(
            call: Call<List<UserRespon>>, response: Response<List<UserRespon>>
        ) {
            if (response.code() == 200) {
                listUser.clear()
                response.body()?.forEach { userResponse ->
                    listUser.add(userResponse)
                }
            } else if (response.code() == 400) {
                print("error login")
                var toast = Toast.makeText(
                    context, "Username atau password salah", Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<List<UserRespon>>, t: Throwable) {
            print(t.message)
        }

    })
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("createuser")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "List User") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn {
                listUser.forEach { user ->
                    item {
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { /* Handle item click if needed */ }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = user.username,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = user.email,
                                        color = Color.White
                                    )
                                }
                                IconButton(modifier = Modifier.size(48.dp), onClick = {
                                    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
                                        .addConverterFactory(GsonConverterFactory.create()).build()
                                        .create(UserService::class.java)
                                    val call = retrofit.delete(user.id)
                                    call.enqueue(object : Callback<UserRespon> {
                                        override fun onResponse(
                                            call: Call<UserRespon>,
                                            response: Response<UserRespon>
                                        ) {
                                            print(response.code())
                                            if (response.code() == 200) {
                                                listUser.remove(user)
                                            } else if (response.code() == 400) {
                                                print("error login")
                                                var toast = Toast.makeText(
                                                    context,
                                                    "Username atau password salah",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<UserRespon>, t: Throwable
                                        ) {
                                            print(t.message)
                                        }

                                    })
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}