package com.example.flightmobileapp


import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.widget.*
import androidx.navigation.Navigation
import androidx.room.Room
import kotlinx.android.synthetic.main.fragment_connection.*
import kotlinx.android.synthetic.main.fragment_connection.view.*
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConnectionFragment : Fragment() {
    private lateinit var viewF : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewF =  inflater.inflate(R.layout.fragment_connection, container, false)

        val button = viewF.button_id
        button.setOnClickListener { onConnectClick()}

        initializeButtons()
        return viewF
    }
    private fun onClick(button: Button){
        //button.text="hey! i changed it"

        val urlText : TextView = viewF.findViewById(R.id.textView3)
        urlText.text = button.text
        CoroutineScope(Dispatchers.IO).launch {
            val dataBase =
                activity?.applicationContext?.let { Room.databaseBuilder(it, AppDB::class.java, "DB").build() }
            val server = ServersEntity()
            server.URL = button.text as String
            server.time = System.currentTimeMillis()
            dataBase?.serverDAO()?.saveServer(server)
        }
        //updateButtons();

    }
    private fun onConnectClick(){

        //Toast.makeText(this@MainActivity, "hiiiii",Toast.LENGTH_SHORT).show()
        //add the URL to the ROOM DB


        val urlText= viewF.textView3
        CoroutineScope(Dispatchers.IO).launch {
            val dataBase =
                activity?.applicationContext?.let { Room.databaseBuilder(it, AppDB::class.java, "DB").build() }
            val server = ServersEntity()
            server.URL = urlText.text.toString()
            server.time = System.currentTimeMillis()
            dataBase?.serverDAO()?.saveServer(server)
        }
        updateButtons()
        val url = urlText.text.toString()
        //now we will check whether or not the image was received successfully
        val retrofit = Retrofit.Builder()
            .baseUrl("http://$url/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {

                val action = ConnectionFragmentDirections.navigateToControl(url)
                Navigation.findNavController(viewF).navigate(action)
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(activity, "failed receiving image from te server http://$url/"
                    + t.message,
                    Toast.LENGTH_LONG).show()
            }
        })

        //updateButtons();
    }

    private fun updateButtons(){

        // this@MainActivity.runOnUiThread{
        //   linear.removeAllViews()
        //}
        //uiScope.launch
        val uiScope= CoroutineScope(Dispatchers.Main + Job())
        uiScope.launch {
            linear.removeAllViews()
        }


        CoroutineScope(Dispatchers.IO).launch{
            val dataBase=
                activity?.applicationContext?.let { Room.databaseBuilder(it,AppDB::class.java,"DB").build() }
            // val serversEntityList:List<ServersEntity> = dataBase.serverDAO().lastFiveConnections(5);
            //var server= ServersEntity()
            //server.URL="Sap"
            //dataBase.serverDAO().saveServer(server)
            //server.URL="jus trying344"
            //dataBase.serverDAO().saveServer(server)
            //dataBase.serverDAO().deleteData(server)
            //var i=0;

            dataBase?.serverDAO()?.lastFiveConnections(5)?.forEach {
                val button= Button(activity)
                button.text=it.URL
                button.isAllCaps=false
                button.setOnClickListener{
                    onClick(button)
                }



                withContext(Dispatchers.Main){(linear?.addView(button))}
            }


        }

    }

    private fun initializeButtons(){
        CoroutineScope(Dispatchers.IO).launch{
            val dataBase=
                activity?.applicationContext?.let { Room.databaseBuilder(it,AppDB::class.java,"DB").build() }
            // val serversEntityList:List<ServersEntity> = dataBase.serverDAO().lastFiveConnections(5);
            //var server= ServersEntity()
            //server.URL="Sap"
            //dataBase.serverDAO().saveServer(server)
            //server.URL="jus trying344"
            //dataBase.serverDAO().saveServer(server)
            //dataBase.serverDAO().deleteData(server)
            dataBase?.serverDAO()?.lastFiveConnections(5)?.forEach {
                val button= Button(activity)
                button.isAllCaps=false
                button.text=it.URL

                button.setOnClickListener{
                    onClick(button)
                }

                withContext(Dispatchers.Main){(linear.addView(button))}
            }

        }

    }
}