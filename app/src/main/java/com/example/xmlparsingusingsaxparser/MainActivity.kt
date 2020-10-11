package com.example.xmlparsingusingsaxparser

import android.os.Bundle
import android.view.View
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory


class MainActivity : AppCompatActivity() {
    var userList = ArrayList<HashMap<String, String?>>()
    var user = HashMap<String, String?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            val lv = findViewById<View>(R.id.user_list) as ListView
            val parserFactory = SAXParserFactory.newInstance()
            val parser = parserFactory.newSAXParser()
            val handler: DefaultHandler = object : DefaultHandler() {
                var currentValue = ""
                var currentElement = false

                @Throws(SAXException::class)
                override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
                    currentElement = true
                    currentValue = ""
                    if (localName == "user") {
                        user = HashMap()
                    }
                }

                @Throws(SAXException::class)
                override fun endElement(uri: String, localName: String, qName: String) {
                    currentElement = false
                    if (localName.equals("name", ignoreCase = true)) user["name"] = currentValue
                    else if (localName.equals("designation", ignoreCase = true)) user["designation"] = currentValue
                    else if (localName.equals("user", ignoreCase = true)) userList.add(user)
                }

                @Throws(SAXException::class)
                override fun characters(ch: CharArray, start: Int, length: Int) {
                    if (currentElement) {
                        currentValue = currentValue + String(ch, start, length)
                    }
                }
            }
            val istream = assets.open("userdetails.xml")
            parser.parse(istream, handler)
            val adapter: ListAdapter = SimpleAdapter(this@MainActivity, userList, R.layout.list,
                arrayOf("name", "designation"), intArrayOf(R.id.name, R.id.designation))
            lv.adapter = adapter
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        }
    }
}