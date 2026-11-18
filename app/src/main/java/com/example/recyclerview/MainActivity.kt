package com.example.recyclerview

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.recyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val _arWayang = ArrayList<dcWayang>()
    private lateinit var adapterWayang: AdapterRecView

    private var _nama: Array<String> = arrayOf()
    private var _karakter: Array<String> = arrayOf()
    private var _deskripsi: Array<String> = arrayOf()
    private var _gambar: Array<String> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        siapkanData()
        tambahData()
        tampilkanData()

        binding.btnToggleLayout.setOnCheckedChangeListener { _, _ ->
            tampilkanData()
        }

        binding.btnTambah.setOnClickListener {
            val container = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(40, 20, 40, 10)
            }

            val etNamaWayang = EditText(this).apply { hint = "Nama Wayang" }
            val etDeskripsiWayang = EditText(this).apply { hint = "Deskripsi Wayang" }
            val etKarakterUtamaWayang = EditText(this).apply { hint = "Karakter Utama Wayang" }
            val etGambarWayang = EditText(this).apply { hint = "Link Gambar Wayang" }

            container.addView(etNamaWayang)
            container.addView(etDeskripsiWayang)
            container.addView(etKarakterUtamaWayang)
            container.addView(etGambarWayang)

            AlertDialog.Builder(this)
                .setTitle("Tambah Data Wayang")
                .setView(container)
                .setPositiveButton("Simpan") { _, _ ->
                    val wayangBaru = dcWayang(
                        foto = etGambarWayang.text.toString(),
                        nama = etNamaWayang.text.toString(),
                        karakter = etKarakterUtamaWayang.text.toString(),
                        deskripsi = etDeskripsiWayang.text.toString()
                    )
                    _arWayang.add(wayangBaru)
                    adapterWayang.notifyItemInserted(_arWayang.size - 1)
                    binding.rvWayang.scrollToPosition(_arWayang.size - 1)
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun siapkanData() {
        _nama = resources.getStringArray(R.array.namaWayang)
        _karakter = resources.getStringArray(R.array.karakterUtamaWayang)
        _deskripsi = resources.getStringArray(R.array.deskripsiWayang)
        _gambar = resources.getStringArray(R.array.gambarWayang)
    }

    private fun tambahData() {
        _arWayang.clear()
        for (i in _nama.indices) {
            val wayang = dcWayang(
                foto = _gambar[i],
                nama = _nama[i],
                karakter = _karakter[i],
                deskripsi = _deskripsi[i]
            )
            _arWayang.add(wayang)
        }
    }

    private fun tampilkanData() {
        if (binding.btnToggleLayout.isChecked) {
            binding.rvWayang.layoutManager = StaggeredGridLayoutManager(
                2,
                LinearLayoutManager.VERTICAL
            )
        } else {
            binding.rvWayang.layoutManager = LinearLayoutManager(this)
        }

        binding.rvWayang.itemAnimator = DefaultItemAnimator().apply {
            removeDuration = 400L
            addDuration = 400L
        }

        adapterWayang = AdapterRecView(_arWayang)
        adapterWayang.setOnItemClickCallback(object : AdapterRecView.OnItemClickCallback {
            override fun onItemClicked(data: dcWayang) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                    putExtra("nama", data.nama)
                    putExtra("karakter", data.karakter)
                    putExtra("deskripsi", data.deskripsi)
                    putExtra("foto", data.foto)
                }
                startActivity(intent)
            }

            override fun delData(pos: Int) {
                val item = _arWayang[pos]
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Konfirmasi HAPUS DATA")
                    .setMessage("Apakah Benar Data ${item.nama} akan dihapus ? ")
                    .setPositiveButton("Hapus") { _, _ ->
                        _arWayang.removeAt(pos)
                        adapterWayang.notifyItemRemoved(pos)
                        adapterWayang.notifyItemRangeChanged(pos, _arWayang.size - pos)
                        Toast.makeText(
                            this@MainActivity,
                            "${item.nama} berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        })

        binding.rvWayang.adapter = adapterWayang
    }
}