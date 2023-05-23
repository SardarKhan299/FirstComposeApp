package com.traiden.mycomposeapp

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.traiden.mycomposeapp.ui.theme.MyComposeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // starting point..//
        setContent {
            MyComposeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Red
                ) {
                    DefaultPreview()
                    //NamePicker(header = "My test App", names = mutableListOf("Test1","Test2","Test3","Test4"), onNameClicked = okBtnCallback())
                }
            }
        }
    }

    private fun okBtnCallback(): (String) -> Unit {
        return {
            Log.d(MainActivity::class.simpleName, "okBtnCallback: $it")
        }
    }
}

@Composable
fun Greeting(name: String) {
    // when this composable will re-compose remember make sure that this
    // value will not be reset during re-compose.///
    var expanded by remember { mutableStateOf(false) }
    // For Animation...//
    val extraPadding by animateDpAsState (
        targetValue = if(expanded) 48.dp else 0.dp,
        animationSpec = tween(2000)
    )

    Surface (color = MaterialTheme.colors.primary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)){
        Row (modifier = Modifier.padding(24.dp)){
            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(bottom = extraPadding)
            ) {
                Text(
                    text = "Hello,", modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(1f)
                )
                Text(
                    text = "$name!", style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.ExtraBold) ,modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.5f)
                )
            }
            OutlinedButton(onClick = { expanded = !expanded}) {
                Text(if (expanded) "Show Less" else "Show More")
            }
        }
    }
}

@Composable
fun OnboardingScreen(onContinueClicked:()->Unit ) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = {onContinueClicked.invoke()}
        ) {
            Text("Continue")
        }
    }
}
@Preview(showBackground = true, widthDp = 320, heightDp = 320, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    MyComposeAppTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}

@Composable
fun MyApp(){
    // check if onBoarding screen is visible or not...//
    // remember saveAble will remember the state and also for configuration changes...//
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
    if(shouldShowOnboarding){
        OnboardingScreen(onContinueClicked = {
            shouldShowOnboarding = false
        })
    }else{
        //Greetings(names = mutableListOf("Android","Compose"))
        Greetings(names = List(1000){"$it"})
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    // My App Contains All the Screens..//
    MyApp()
}

@Composable
fun Greetings(names:List<String>){
    MyComposeAppTheme {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
//            for(name in names){
//                Greeting(name)
//            }

            // Recyclerview it will create only those composables that are currently on screen...//
            LazyColumn{
                // Work as Header..//
                item{ Text("Header") }
                items(names){ name->
                    Greeting(name)
                }
            }
        }

    }
}

/**
 * Display a list of names the user can click with a header
 */
@Composable
fun NamePicker(
    header: String,
    names: List<String>,
    onNameClicked: (String) -> Unit
) {
    Column {
        // this will recompose when [header] changes, but not when [names] changes
        Text(header, style = MaterialTheme.typography.body1)
        Divider()

        // LazyColumn is the Compose version of a RecyclerView.
        // The lambda passed to items() is similar to a RecyclerView.ViewHolder.
        LazyColumn {
            items(names) { name ->
                // When an item's [name] updates, the adapter for that item
                // will recompose. This will not recompose when [header] changes
                NamePickerItem(name, onNameClicked)
                Divider()
            }
        }
    }
}

/**
 * Display a single name the user can click.
 */
@Composable
private fun NamePickerItem(name: String, onClicked: (String) -> Unit) {
    Text(name, Modifier.clickable(onClick = { onClicked(name) }))
}