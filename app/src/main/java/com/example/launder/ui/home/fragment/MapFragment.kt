package com.example.launder.ui.home.fragment


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.agent.R
import com.example.agent.databinding.FragmentTrackingBinding
import com.example.launder.ui.auth.AuthViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment :Fragment(R.layout.fragment_tracking){

    private lateinit var binding: FragmentTrackingBinding
    lateinit var viewModel: AuthViewModel
    private lateinit var map:GoogleMap
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    val sydney = LatLng(-34.0, 151.0)
    val stokeOnTrent = LatLng(53.025780,-2.177390)
    val stokeCobrdige = LatLng(53.029380,-2.188740)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentTrackingBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)


        binding.mapView.getMapAsync {
            map = it

            drawPolyline()
        }


    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }
    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }
    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }




    private fun drawPolyline() {
        val nairobi = LatLng(-1.3997322336247418, 36.78874316846644) // Nairobi coordinates
        val k = LatLng(-1.3991210978278568, 36.79044435244359)
        val j = LatLng(-1.3991210978278568, 36.79044435244359)
        val i = LatLng(-1.3998236269699476, 36.78926418046837)
        val h = LatLng(-1.4000810421716021, 36.78919980745456)
        val g = LatLng(-1.4001346703350508, 36.789071061426924)
        val f = LatLng(-1.3999147948522477, 36.7888833067681)
        val e = LatLng(-1.4004551321856535, 36.787241770641984)
        val d = LatLng(-1.4016027744278658, 36.78452737511848)
        val c = LatLng(-1.4016242256794746, 36.7841089505137)
        val b = LatLng(-1.4014097131328396, 36.78372271241383)
        val a = LatLng(-1.396508096520164, 36.77863724413161)
        val twl = LatLng(-1.3953497260699057, 36.7799568909534)
        val elev = LatLng(-1.393601443749429, 36.78094394386726)
        val ten = LatLng(-1.3918960628121697, 36.78216703117356)
        val nine = LatLng(-1.3930866118984209, 36.778476311582615)
        val eight = LatLng(-1.3911211489970992, 36.77462735841911)
        val seven = LatLng(-1.391005469153234, 36.77259721061417)
        val six = LatLng(-1.3907346458557528, 36.77012957832107)
        val five = LatLng(-1.3907668228810874, 36.76883675357321)
        val four = LatLng(-1.390799, 36.768103)
        val three = LatLng(-1.3905061358456057, 36.76690277227135)
        val two = LatLng(-1.3925463406339267, 36.76545927488378)
        val one = LatLng(-1.394293661399087, 36.7643479132145)
        val final = LatLng(-1.394029186000446, 36.76406990761305) // Mombasa coordinates
        val al = LatLng(-1.4121104175131927, 36.78346036161183) // Mombasa coordinates
        val polylineOptions = PolylineOptions()
            .add(final,one, two,three,four,five,six,seven,eight,nine,ten,elev,twl,a,b,c,d,e,f,g,h,i,j,k)
            .color(Color.RED)
            .width(5f)

        map?.addPolyline(polylineOptions)

        // Move the camera to the midpoint of the polyline
        val midpointLat = (nairobi.latitude + final.latitude) / 2
        val midpointLng = (nairobi.longitude + final.longitude) / 2
        val midpoint = LatLng(midpointLat, midpointLng)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(midpoint, 14f))
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }



}
































