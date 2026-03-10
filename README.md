# Andkot-BindService002Messenger
AndroidのService(Message編)のサンプルコード

## 状態
|開始状態|バインド状態|開始/バインド状態|
|---|---|---|
|startService()|bindService()|startService()<br/>bindService()|

|Binderクラス拡張サンプル<br/>Andkot-BindService001BinderEx|Message使用サンプル<br/>Andkot-BindService002Messenger|aidlサンプル<>|
|Binderクラス拡張サンプル<br/>Andkot-BindService001BinderEx|Message使用サンプル<br/>Andkot-BindService002Messenger|aidlサンプル<br/>Andkot-BindService003Aidl|
|---|---|---|
|戻り値BinderクラスにServiceクラスの実体を渡してやり取り。<br/>意外と簡単。|1.onBind()にて、サービスのMessengerをActivityに渡す<br/>2.onServiceConnected()でそれを受け取って使う。<br/>3.受信側(Activity←Service)は、onServiceConnected()で送信できるようになったので、Replytoにセットして送る。<br/>4.handleMessage()に届くのでMessangeとして保持する。|1.モジュールのbuild.gradle.ktsに```buildGeatures { aidl=true }```を追加。<br/>2.aidlを作る(送信用とCallback用)。<br/>3.ServiceConnectionクラスを生成(_con)。<br/>4.bindService(_con)でバインド(バインド状態)。<br/>これで送受信が可能になる。<br/>|
