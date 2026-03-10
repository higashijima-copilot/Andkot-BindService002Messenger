package com.aaa.bindservice002messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
  private var _toServiceMessenger: Messenger? = null
  private var _bound: Boolean = false
  private val _com = object: ServiceConnection {
    override fun onServiceConnected(name: ComponentName, binder: IBinder) {
      Log.d("aaaaa", "aaaaa onServiceConnected()")
      _toServiceMessenger = Messenger(binder)
      _bound = true
      /* 接続要求 */
      var msg: Message = Message.obtain(null, MSG_REGISTER_CLIENT_REQ)
      msg.replyTo = _fromServiceMessenger
      _toServiceMessenger?.send(msg)
      /* メッセージ送信 */
      var msg2: Message = Message.obtain(null, MSG_SET_VALUE, this.hashCode(), 0)
      _toServiceMessenger?.send(msg2)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
      _toServiceMessenger = null
      _bound = false
    }
  }

  val _fromServiceMessenger: Messenger = Messenger(IncomingHandler())
  class IncomingHandler : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
      when (msg.what){
        MSG_SET_VALUE -> Log.d("aaaaa", "aaaaa msg.arg1=$msg.arg1")
        else -> super.handleMessage(msg)
      }
    }
  }

  private fun sayhello() {
    if(!_bound) return
    val msg: Message? = Message.obtain(null, MSG_SAY_HELLO, 0, 0)
    _toServiceMessenger?.send(msg)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_main)
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
      insets
    }

    findViewById<Button>(R.id.btn_sayhello).setOnClickListener {
      sayhello()
    }
  }

  override fun onStart() {
    super.onStart()
    Log.d("aaaaa", "aaaaa bindService()")
    bindService(Intent(this, MessengerService::class.java), _com, Context.BIND_AUTO_CREATE)
  }

  override fun onStop() {
    super.onStop()
    if(_bound) {
      _toServiceMessenger?.send(Message.obtain(null, MSG_UNREGISTER_CLIENT_REQ).apply {
        replyTo = _fromServiceMessenger
      })
      Log.d("aaaaa", "aaaaa unbindService()")
      unbindService(_com)
      _bound = false
    }
  }
}