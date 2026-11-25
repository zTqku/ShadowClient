package com.shadowclient.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class MainActivity : Activity() {
    private val bg = Color.rgb(17, 19, 28)
    private val panel = Color.rgb(35, 39, 52)
    private val accent = Color.rgb(139, 92, 246)
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildScreen()
    }

    private fun buildScreen() {
        val scroll = ScrollView(this).apply { setBackgroundColor(bg) }
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 36, 24, 32)
        }

        val title = TextView(this).apply {
            text = "SHADOW CLIENT"
            textSize = 27f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
        }
        page.addView(title)

        val subtitle = TextView(this).apply {
            text = "BEDROCK RESOURCE MANAGER  •  v1.0"
            textSize = 11f
            setTextColor(Color.LTGRAY)
            setPadding(0, 6, 0, 24)
        }
        page.addView(subtitle)

        val intro = TextView(this).apply {
            text = "Tu cliente. Tu estilo.\\n\\nEsta primera versión prepara y exporta el paquete de recursos de Shadow Client para abrirlo con Minecraft."
            textSize = 15f
            setTextColor(Color.WHITE)
            setPadding(18, 18, 18, 18)
            setBackgroundColor(panel)
        }
        page.addView(intro)

        val heading = TextView(this).apply {
            text = "MÓDULOS DEL PROYECTO"
            textSize = 13f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(190, 170, 255))
            setPadding(0, 28, 0, 12)
        }
        page.addView(heading)

        listOf(
            "Fullbright — pendiente de implementación compatible",
            "Custom HUD — en desarrollo",
            "Coordinates — requiere soporte de Bedrock",
            "Custom Crosshair — personalización visual",
            "PvP Textures — paquete inicial"
        ).forEach { label ->
            val item = TextView(this).apply {
                text = "•  $label"
                textSize = 14f
                setTextColor(Color.WHITE)
                setPadding(16, 15, 16, 15)
                setBackgroundColor(panel)
            }
            val params = LinearLayout.LayoutParams(-1, -2)
            params.bottomMargin = 8
            page.addView(item, params)
        }

        val export = Button(this).apply {
            text = "EXPORTAR PAQUETE PARA MINECRAFT"
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundColor(accent)
            setOnClickListener { exportAndOpenPack() }
        }
        val exportParams = LinearLayout.LayoutParams(-1, -2)
        exportParams.topMargin = 24
        page.addView(export, exportParams)

        status = TextView(this).apply {
            text = "Estado: listo para exportar"
            textSize = 13f
            setTextColor(Color.LTGRAY)
            setPadding(2, 18, 2, 4)
        }
        page.addView(status)

        val note = TextView(this).apply {
            text = "Shadow Client no modifica el APK de Minecraft ni activa trucos. La disponibilidad de logros depende de Minecraft y de los recursos importados."
            textSize = 12f
            setTextColor(Color.LTGRAY)
            setPadding(0, 22, 0, 0)
        }
        page.addView(note)

        scroll.addView(page)
        setContentView(scroll)
    }

    private fun exportAndOpenPack() {
        try {
            val exportDir = File(cacheDir, "exports").apply { mkdirs() }
            val packFile = File(exportDir, "ShadowClient.mcpack")
            ZipOutputStream(FileOutputStream(packFile)).use { zip ->
                addAssetFolderToZip("pack", "", zip)
            }

            val uri = FileProvider.getUriForFile(
                this,
                "$packageName.fileprovider",
                packFile
            )
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/zip")
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            status.text = "Paquete creado. Elegí Minecraft para importarlo."
            startActivity(android.content.Intent.createChooser(intent, "Abrir paquete con"))
        } catch (e: Exception) {
            status.text = "No se pudo exportar: ${e.localizedMessage ?: "error"}"
            Toast.makeText(this, status.text, Toast.LENGTH_LONG).show()
        }
    }

    private fun addAssetFolderToZip(assetPath: String, zipPath: String, zip: ZipOutputStream) {
        val children = assets.list(assetPath) ?: emptyArray()
        if (children.isEmpty()) {
            assets.open(assetPath).use { input ->
                zip.putNextEntry(ZipEntry(zipPath))
                input.copyTo(zip)
                zip.closeEntry()
            }
            return
        }
        for (child in children) {
            val childAsset = "$assetPath/$child"
            val childZip = if (zipPath.isEmpty()) child else "$zipPath/$child"
            val nested = assets.list(childAsset) ?: emptyArray()
            if (nested.isNotEmpty()) {
                addAssetFolderToZip(childAsset, "$childZip/", zip)
            } else {
                assets.open(childAsset).use { input ->
                    zip.putNextEntry(ZipEntry(childZip))
                    input.copyTo(zip)
                    zip.closeEntry()
                }
            }
        }
    }
}
