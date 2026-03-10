package com.aaa.bindservice002messenger

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast


const val MSG_REGISTER_CLIENT_REQ = 1
const val MSG_UNREGISTER_CLIENT_REQ = 2
const val MSG_SAY_HELLO = 3
const val MSG_SET_VALUE = 4

class MessengerService : Service() {
  private lateinit var _rcvMessenger: Messenger
  private val _clients: ArrayList<Messenger> = ArrayList()
  /* IncomingHandlerクラス */
  internal class IncomingHandler(private val context: Context, private val _applicationContext: Context = context.applicationContext)
      : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
      when(msg.what) {
        MSG_REGISTER_CLIENT_REQ -> {
          Log.d("aaaaa", "aaaaa Regist OK!!")
          if(context is MessengerService)
            context._clients.add(msg.replyTo)
        }
        MSG_UNREGISTER_CLIENT_REQ -> {
          Log.d("aaaaa", "aaaaa Unregist OK!!")
          if(context is MessengerService)
            context._clients.remove(msg.replyTo)
        }
        MSG_SAY_HELLO -> {
          Log.d("aaaaa", "aaaaa activity2service: MSG_SAY_HELLO")
          Toast.makeText(_applicationContext, "hello!", Toast.LENGTH_SHORT).show()
        }
        MSG_SET_VALUE -> {
          Log.d("aaaaa", "aaaaa activity2service: MSG_SAY_HELLO")
          if(context is MessengerService) {
            context._clients.forEach{ messenger ->
              run {
                messenger.send(Message.obtain(null, MSG_SET_VALUE, msg.arg1, 0))
              }
            }
          }
        }
        else -> super.handleMessage(msg)
      }
    }
  }

  /* onBind() */
  override fun onBind(intent: Intent): IBinder {
    Log.d("aaaaa", "aaaaa MessengerService::onBind()")
    _rcvMessenger = Messenger(IncomingHandler(this))
    return _rcvMessenger.binder  /* ←ここがミゾ。MessengerもIBinderクラスを継承しているという  */
  }
}
