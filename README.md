# jsp-trustapp

* [Introduction](#introduction)
* [Module](#Module)
	* [Authentication](#Authentication)
	* [Count](#Count)
	* [Count_Time](#Count_Time)
	* [GetTestRegID](#GetTestRegID)
	* [Identification](#Identification)
	* [Identification_or_not](#Identification_or_not)
	* [ReceiveFromClient](#ReceiveFromClient)
	* [RequestList](#RequestList)
	* [SendToGCM](#SendToGCM)
	* [Time](#Time)

<h2 id = "introduction">Introduction</h2>

App 應用服務可移轉性驗證

目的為達成原授權者可透過無線通訊進行移轉 App 之憑證，接收者可透過擁有此 App 之憑證向伺服器下載此 App，並達到驗證移轉雙方之身份、接收者下載時間之限制、憑證移轉次數之限制等安全功能。

<h2 id = "Module">Module</h2>

<h3 id = "Authentication">Authentication</h3>

用 `username` 和 `password` 做登入驗證。

<h3 id = "Count">Count</h3>

次數判別，

用 `username` 和 `app_id` 去資料庫取得使用者擁有的此 App 還剩多少下載次數。

<h3 id = "Count_Time">Count_Time</h3>

時間及次數判別，

用 `username` 和 `app_id` 去資料庫同時判別是否還有下載次數、是否在合法下載的時間區間內。

<h3 id = "GetTestRegID">GetTestRegID</h3>

用硬體獨有的 `mac_address` 去取得註冊時對應的 registration ID。

<h3 id = "Identification">Identification</h3>

硬體辨識，

用 `username` 和 `identification` 去資料庫進行比對，

`identification` 由 Mac Address, Android ID, IMEI，合在一起做 SHA1 而成。

<h3 id = "Identification_or_not">Identification_or_not</h3>

由開發者決定此次辨識限定於哪個時間區間內、是否要進行次數判別。

<h3 id = "ReceiveFromClient">ReceiveFromClient</h3>

註冊 [GCM](https://developers.google.com/cloud-messaging/) 時，將 `Token` 及對應的 `mac_address` 寫入資料庫。

<h3 id = "RequestList">RequestList</h3>

傳送 `username` 及對應的 `mac_address` 以回傳給前端，讓使用者選擇可移轉的對象。

<h3 id = "SendToGCM">SendToGCM</h3>

Android Server 端透過 HTTP 推播訊息給 GCM

參考 [Google Cloud Messaging (GCM) HTTP Connection Server](https://developers.google.com/cloud-messaging/http#auth)

步驟：

1. 註冊 google 帳號

2. 開啟 GCM 服務

3. 生成一個 Android API Key

4. 跟接收者要 Registration ID

[POST request](https://gcm-http.googleapis.com/gcm/send)

HTTP header 規定內容：

```
httpPost.addHeader("Authorization", "key=" + apiKey);
httpPost.addHeader("Content-Type", "application/json");
```

將訊息包成 JSON 格式送出，並且要完全符合以下格式

```
{
    "to" : "Registration_ID",
    "data" : {
        "data_name_1" : "...",
        "data_name_2" : "...",
        ...
    },
}
```

<h3 id = "Time">Time</h3>

三種時間區間判別

1. 時間前：在某個特定日期以前下載，即合法。

2. 時間後：在某個特定日期之後下載，即合法。

3. 時間區間：在某兩個日期的區間內下載，即合法。