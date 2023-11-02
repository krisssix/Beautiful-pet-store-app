package cz.mendelu.pef.petstore.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramcosta.composedestinations.DestinationsNavHost
import cz.mendelu.pef.petstore.NavGraphs
import cz.mendelu.pef.petstore.ui.theme.PetStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        SSLUtil.trustEveryone()
        super.onCreate(savedInstanceState)
        setContent {
            PetStoreTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)

            }
        }
    }
}
