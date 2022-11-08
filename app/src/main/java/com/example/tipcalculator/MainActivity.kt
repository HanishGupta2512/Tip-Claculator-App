package com.example.tipcalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.components.inputfield
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import com.example.tipcalculator.utill.calculatetotalperperson
import com.example.tipcalculator.utill.calculatetotaltip
import com.example.tipcalculator.widgets.roundiconbutton

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          MyApp {
              maincontent()
          }
        }
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit){

    TipCalculatorTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@Composable
fun TopHeader(totalperperson : Double = 198.0 ){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)
        .height(150.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
         color = Color(0xFFE9D7F7)) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalperperson)
            Text(text = "Total Per Person",
                 style = MaterialTheme.typography.h5)
            Text(text = "$$total",
                style = MaterialTheme.typography.h4,
                 fontWeight = FontWeight.ExtraBold)
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun maincontent() {
    val splitbystate = remember {
        mutableStateOf(1)
    }

    val range = IntRange(1, 100)

    val tipamountstate = remember {
        mutableStateOf(0.0)
    }

    val totalperpersonstate = remember {
        mutableStateOf(0.0)
    }
    Column(modifier = Modifier.padding(all = 12.dp)) {
        billform(splitbystate = splitbystate,
                range = range,
                 tipamountstate = tipamountstate,
                 totalperpersonstate = totalperpersonstate ) {}
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun billform(modifier: Modifier = Modifier,
             range: IntRange = 1..100,
             splitbystate: MutableState<Int>,
             tipamountstate: MutableState<Double>,
             totalperpersonstate: MutableState<Double>,
             onValChange:(String) -> Unit = {}) {



    val totalbillstate = remember{
        mutableStateOf("")
    }
    val validstate = remember(totalbillstate.value){
        totalbillstate.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderpositionstate = remember {
        mutableStateOf(0f)
    }

    val tippercentage = (sliderpositionstate.value * 100).toInt()

    TopHeader(totalperperson = totalperpersonstate.value)

    Surface(modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)) {

        Column(modifier = Modifier.padding(6.dp),
               verticalArrangement = Arrangement.Top,
               horizontalAlignment = Alignment.Start) {


            inputfield(valueState = totalbillstate ,
                labelId = "Enter Bill" ,
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions{
                    if(!validstate) return@KeyboardActions
                    onValChange(totalbillstate.value.trim())

                    keyboardController?.hide()


                })
            if (validstate) {
                Row(modifier = modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start) {
                    Text(text = "Split",
                         modifier = Modifier.align(
                             alignment = Alignment.CenterVertically
                         ))
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End) {
                        roundiconbutton(imageVector = Icons.Default.Remove,
                                        onClick = {
                                            splitbystate.value =
                                                if(splitbystate.value > 1) splitbystate.value - 1
                                            else 1
                                            totalperpersonstate.value =
                                                calculatetotalperperson(totalbill = totalbillstate.value.toDouble(),
                                                    splitby = splitbystate.value,
                                                    tippercentage = tippercentage)

                                        })

                        Text(text = "${splitbystate.value}",
                             modifier = modifier
                                 .align(Alignment.CenterVertically)
                                 .padding(start = 9.dp, end = 9.dp))


                        roundiconbutton(imageVector = Icons.Default.Add,
                            onClick = {
                                if (splitbystate.value < range.last){
                                    splitbystate.value = splitbystate.value + 1
                                    totalperpersonstate.value =
                                        calculatetotalperperson(totalbill = totalbillstate.value.toDouble(),
                                            splitby = splitbystate.value,
                                            tippercentage = tippercentage)
                                }

                            })

                    }



                }


//            tip row
            Row(modifier = modifier
                .padding(horizontal = 3.dp, vertical = 12.dp)) {
                Text(text = "Tip",
                     modifier = Modifier.align(alignment = Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(200.dp))
                
                Text(text = "$ ${tipamountstate.value}",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically))

                
            }
            Column(verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$tippercentage %")

                Spacer(modifier = Modifier.height(14.dp))

//                slider
                Slider(value = sliderpositionstate.value,
                       onValueChange ={ newVal ->
                           sliderpositionstate.value = newVal
                           tipamountstate.value =
                               calculatetotaltip(totalbill = totalbillstate.value.toDouble(),
                                                 tippercentage = tippercentage)

                           totalperpersonstate.value =
                               calculatetotalperperson(totalbill = totalbillstate.value.toDouble(),
                                                 splitby = splitbystate.value,
                                              tippercentage = tippercentage)

                       },
                         modifier = Modifier.padding(start = 16.dp,
                                                     end = 16.dp),
                         steps = 5,
                         onValueChangeFinished = {
//                             Log.d(TAG, "billform: Finished...")
                         })


            }



            }else {
                Box(){}
            }

        }

    }
}


