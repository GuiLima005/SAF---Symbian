package br.senai.sp.jandira.saf

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.saf.ui.theme.SAFTheme
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SAFTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Login()
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(modifier: Modifier = Modifier) {

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    val context = LocalContext.current

    val storage = Firebase.storage
    val storageRef = storage.reference

    Column(modifier = Modifier.fillMaxSize()) {

        var photoUri by remember {
            mutableStateOf<Uri?>(null)
        }

        // variavel que vai pegar a URI
        var launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            photoUri = uri


            var file = uri

            val photo = storageRef.child("${file!!.lastPathSegment}")

            var upload = photo.putFile(file!!)

            val urlTask = upload.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                photo.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {
                    // Handle failures
                    // ...
                }
            }
        }

        // variavel que vai transforma a URI em uma image
        var painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(photoUri)
                .build()
        )

        Column(
            modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(30.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 28.dp)
            ) {
                Text(
                    text = "Symbian",
                    modifier = modifier.fillMaxWidth(),
                    fontSize = 36.sp,
                    fontWeight = FontWeight(800),
                    textAlign = TextAlign.Center,
                    color = Color(146, 184, 255)
                )
            }

            Spacer(modifier = Modifier.height(70.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box() {
                    Card(
                        modifier = Modifier.size(110.dp),
                        shape = CircleShape,
                        border = BorderStroke(3.5.dp, Color(146, 184, 255))

                    ) {
                        Image(
                            painter = if (photoUri == null) {
                                painterResource(id = R.drawable.login)
                            } else {
                                painter
                            },
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .clickable { launcher.launch("image/*") }

                        )
                    }
                }

                Spacer(modifier = Modifier.height(35.dp))


                Column() {
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        modifier = Modifier
                            .width(355.dp)
                           ,
                        label = {
                            Text(text = "Email",
                                fontWeight = FontWeight(600)
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = Color(146, 184, 255),
                            unfocusedIndicatorColor = Color(146, 184, 255)
                        ),
                        singleLine = true

                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    OutlinedTextField(
                        value = senha,
                        onValueChange = {
                            senha = it
                        },
                        modifier = Modifier
                            .width(355.dp),
                        label = {
                                Text(text = "Senha",
                                    fontWeight = FontWeight(600))
                        },
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = Color(146, 184, 255),
                            unfocusedIndicatorColor = Color(146, 184, 255)
                        ),
                        singleLine = true

                    )
                }

                Spacer(modifier = Modifier.height(27.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Button(
                        onClick =
                        {
                            var login = Response(
                                login = email,
                                senha = senha,
                                imagem = "${photoUri}"
                            )

                        var call = RetrofitFactory().Login().insertLogin(login)

                            call.enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: retrofit2.Response<ResponseBody>
                                ) {

                                    Log.i("Sucesso", "${response}")

                                    val backgroundColor = Color.Gray
                                    val contentColor = Color.White

                                    val toast = Toast(context)
                                    toast.setGravity(Gravity.CENTER, 0, 20)
                                    toast.duration = Toast.LENGTH_SHORT

                                    val textView = TextView(context).apply {
                                        text = "Conta criada com sucesso"
                                        textSize = 18f // Tamanho da fonte aumentado
                                        setBackgroundColor(backgroundColor.toArgb()) // Converter a cor para ARGB
                                        setTextColor(contentColor.toArgb()) // Converter a cor para ARGB
                                        setPadding(
                                            36,
                                            36,
                                            36,
                                            36
                                        ) // Valores inteiros em pixels para padding
                                    }

                                    toast.view = textView
                                    toast.show()

                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.i("Erro", "${t.message}")
                                }
                            })

                        },
                        modifier = Modifier
                            .width(300.dp)
                            .height(53.dp),
                        colors = ButtonDefaults.buttonColors(Color(146, 184, 255)),

                        shape = RoundedCornerShape(16.dp),

                        ) {
                        Text(
                            text = "Criar Conta",
                            color = Color.White,
                            fontWeight = FontWeight(700),
                            fontSize = 22.sp
                        )
                    }

                }


            }

        }


    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Login()
}