# jsp-trustapp

* [Introduction](#introduction)
* [Module](#Module)

<h2 id = "introduction">Introduction</h2>

App 應用服務可移轉性驗證

目的為達成原授權者可透過無線通訊進行移轉 App 之憑證，接收者可透過擁有此 App 之憑證向伺服器下載此 App，並達到驗證移轉雙方之身份、接收者下載時間之限制、憑證移轉次數之限制等安全功能。

<h2 id = "Module">Module</h2>

**Count**

用 `username` 和 `app_id` 去資料庫取得使用者擁有的此 App 還剩多少下載次數。

**Count_Time**

用 `username` 和 `app_id` 去資料庫同時判別是否還有下載次數、是否在合法下載的時間區間內。

